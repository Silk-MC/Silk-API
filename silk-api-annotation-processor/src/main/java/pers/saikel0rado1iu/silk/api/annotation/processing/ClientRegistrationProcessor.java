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

package pers.saikel0rado1iu.silk.api.annotation.processing;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pers.saikel0rado1iu.silk.api.annotation.processing.ProcessorUtil.getTypeElement;

/**
 * <h2 style="color:FFC800">客户端注册处理器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("pers.saikel0rado1iu.silk.api.annotation.ClientRegistration")
public final class ClientRegistrationProcessor extends AbstractProcessor {
	static Optional<TypeSpec.Builder> generateMethod(Optional<TypeSpec.Builder> optionalBuilder, Element element, ProcessingEnvironment processingEnv, ClientRegistration clientRegistration) {
		TypeElement registrar = getTypeElement(processingEnv, clientRegistration::registrar);
		if (registrar == null) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("未找到注册器：%s", clientRegistration.registrar()), element);
			return Optional.empty();
		}
		TypeElement type = getTypeElement(processingEnv, clientRegistration::type);
		if (type == null) {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("未找到注册类型：%s", clientRegistration.type()), element);
			return Optional.empty();
		}
		TypeSpec.Builder builder = optionalBuilder.orElse(ProcessorUtil.createTypeBuilder(type, element));
		// 注册方法
		// 提取构造方法
		Optional<ExecutableElement> constructorOpt = registrar.getEnclosedElements().stream()
				.filter(e -> e.getKind() == ElementKind.CONSTRUCTOR)
				.map(e -> (ExecutableElement) e)
				.max(Comparator.comparingInt(executable -> executable.getParameters().size()));
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
		if (constructorOpt.isPresent()) {
			// 创建注解
			AnnotationSpec annotationSpec = AnnotationSpec.builder(ClassName.get(processingEnv.getElementUtils().getTypeElement("net.fabricmc.api.Environment"))).addMember(
					"value", "$T.$L",
					processingEnv.getElementUtils().getTypeElement("net.fabricmc.api.EnvType"),
					processingEnv.getElementUtils().getTypeElement("net.fabricmc.api.EnvType").getEnclosedElements().get(0)).build();
			MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("registrar")
					.addJavadoc("""
							客户端注册方法<br>
							提供 {@link Runnable} 进行注册，您应该以如下方式进行客户端注册：<br>
							<pre>{@code
							abstract class ClientFoo implements ClientItemRegistry {
								static {
									ItemRegistry.registrar(() -> ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack)), EXAMPLE_ITEM)).register(EXAMPLE_ITEM);
								}
							\t
								private ClientFoo() {
								}
							}
							}</pre>
							\t
							@param registerMethod   注册方法，在此方法内为所有需要客户端注册的对象进行注册
							@return                 客户端注册器""")
					.addAnnotation(annotationSpec)
					.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
					.addTypeVariable(TypeVariableName.get("T", typeName))
					.returns(TypeName.get(registrar.asType()));
			StringBuilder pars = new StringBuilder();
			for (VariableElement param : constructorOpt.get().getParameters()) {
				TypeMirror paramType = param.asType();
				String paramName = param.getSimpleName().toString();
				if (pars.isEmpty()) pars.append(paramName);
				else pars.append(", ").append(paramName);
				ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(paramType), paramName).build();
				methodBuilder.addParameter(parameterSpec);
			}
			// 添加客户端注册方法
			builder.addMethod(methodBuilder.addStatement("return new $T(" + pars + ")", registrar).build());
			return Optional.of(builder);
		} else {
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("未找到构造方法：%s", clientRegistration.registrar()), element);
			return Optional.empty();
		}
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Elements elementUtils = processingEnv.getElementUtils();
		for (Element element : roundEnv.getElementsAnnotatedWith(ClientRegistration.class)) {
			if (!ProcessorUtil.checkAnnotation(ClientRegistration.class, roundEnv, processingEnv, (TypeElement) element)) return true;
			ClientRegistration clientRegistration = element.getAnnotation(ClientRegistration.class);
			if ("java.lang.Class".equals(getTypeElement(processingEnv, clientRegistration::registrar).getQualifiedName().toString())
					&& "java.lang.Class".equals(getTypeElement(processingEnv, clientRegistration::type).getQualifiedName().toString())) continue;
			// 写入文件
			String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
			Optional<TypeSpec.Builder> optionalBuilder = ClientRegistrationProcessor.generateMethod(Optional.empty(), element, processingEnv, clientRegistration);
			if (optionalBuilder.isEmpty()) return false;
			TypeSpec.Builder builder = optionalBuilder.get();
			ServerRegistration serverRegistration = element.getAnnotation(ServerRegistration.class);
			if (serverRegistration != null) ServerRegistrationProcessor.generateMethod(Optional.of(builder), element, processingEnv, serverRegistration);
			TypeSpec typeSpec = builder.build();
			// 创建一个文件
			try {
				FileObject existingFile = processingEnv.getFiler().getResource(StandardLocation.SOURCE_OUTPUT, packageName, typeSpec.name + ".java");
				if (existingFile != null && existingFile.getLastModified() > 0) return true;
				JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
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