/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.annotation.processing;

import com.squareup.javapoet.*;
import pers.saikel0rado1iu.silk.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.annotation.ServerRegistration;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <h2 style="color:FFC800">服务端注册处理器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@SupportedAnnotationTypes("pers.saikel0rado1iu.silk.annotation.ServerRegistration")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ServerRegistrationProcessor extends AbstractProcessor {
	static Optional<TypeSpec.Builder> generateMethod(Optional<TypeSpec.Builder> optionalBuilder, Element element, ProcessingEnvironment processingEnv, ServerRegistration serverRegistration) {
		TypeElement registrar = processingEnv.getElementUtils().getTypeElement(serverRegistration.registrar());
		if (registrar == null) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("未找到注册器：%s", serverRegistration.registrar()), element);
			return Optional.empty();
		}
		TypeElement type = processingEnv.getElementUtils().getTypeElement(serverRegistration.type());
		if (type == null) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("未找到注册类型：%s", serverRegistration.type()), element);
			return Optional.empty();
		}
		TypeSpec.Builder builder = optionalBuilder.orElse(RegistrationProcessor.createTypeBuilder(type, element));
		// 获取类的类型信息
		TypeMirror typeMirror = type.asType();
		TypeName typeName = TypeName.get(typeMirror);
		// 处理泛型类型
		if (typeMirror instanceof DeclaredType declaredType) {
			List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
			if (!typeArguments.isEmpty()) {
				typeName = ParameterizedTypeName.get(ClassName.get((TypeElement) declaredType.asElement()), typeArguments.stream()
						.map(typeArg -> WildcardTypeName.subtypeOf(Object.class)).toArray(TypeName[]::new));
			}
		}
		// 注册方法
		MethodSpec registrarMethod = MethodSpec.methodBuilder("registrar")
				.addJavadoc("""
						服务端注册方法<br>
						此方法方法一个服务端注册器，注册器注册返回注册对象<br>
						
						@param $N   注册对象
						@return     服务端注册器""", type.getSimpleName().toString().toLowerCase())
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addTypeVariable(TypeVariableName.get("T", typeName))
				.addParameter(TypeVariableName.get("T"), type.getSimpleName().toString().toLowerCase())
				.addStatement("return new $T($N)", registrar, type.getSimpleName().toString().toLowerCase())
				.returns(TypeName.get(registrar.asType()))
				.build();
		// 添加服务端注册方法
		builder.addMethod(registrarMethod);
		return Optional.of(builder);
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Elements elementUtils = processingEnv.getElementUtils();
		for (Element element : roundEnv.getElementsAnnotatedWith(ServerRegistration.class)) {
			if (!RegistrationProcessor.checkAnnotation(ServerRegistration.class, roundEnv, processingEnv, (TypeElement) element)) return true;
			ServerRegistration serverRegistration = element.getAnnotation(ServerRegistration.class);
			if ("root".equals(serverRegistration.registrar()) && "root".equals(serverRegistration.type())) continue;
			if (serverRegistration.registrar().isEmpty()) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "未声明注册器", element);
				return false;
			} else if (serverRegistration.type().isEmpty()) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "未声明注册类型", element);
				return false;
			}
			// 写入文件
			String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
			Optional<TypeSpec.Builder> optionalBuilder = ServerRegistrationProcessor.generateMethod(Optional.empty(), element, processingEnv, serverRegistration);
			if (optionalBuilder.isEmpty()) return false;
			TypeSpec.Builder builder = optionalBuilder.get();
			ClientRegistration clientRegistration = element.getAnnotation(ClientRegistration.class);
			if (clientRegistration != null) ClientRegistrationProcessor.generateMethod(Optional.of(builder), element, processingEnv, clientRegistration);
			TypeSpec provider = builder.build();
			// 创建一个文件
			try {
				FileObject existingFile = processingEnv.getFiler().getResource(StandardLocation.SOURCE_OUTPUT, packageName, provider.name + ".java");
				if (existingFile != null && existingFile.getLastModified() > 0) return true;
				JavaFile javaFile = JavaFile.builder(packageName, provider).build();
				try {
					javaFile.writeTo(processingEnv.getFiler());
				} catch (IOException e) {
					processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("无法生成源码：%s", e.getMessage()));
					return false;
				}
			} catch (IOException e) {
				processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, String.format("%s 生成文件被占用，可能是代码正在生成中：%s", element, e.getMessage()));
			}
		}
		return true;
	}
}