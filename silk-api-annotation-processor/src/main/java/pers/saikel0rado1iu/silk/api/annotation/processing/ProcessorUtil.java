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

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">处理器实用工具</h2>
 * 用于提供一些简单的注册注解处理器方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
interface ProcessorUtil {
	/**
	 * 将字符串转换为大驼峰命名法的字符串格式
	 *
	 * @param str 需转换的字符串
	 * @return 转换后的字符串
	 */
	static String capitalize(String str) {
		if (str == null || str.isEmpty()) return str;
		str = str.replaceAll("_", " ");
		String[] words = str.split("\\s+");
		StringBuilder sb = new StringBuilder();
		for (String word : words) if (!word.isEmpty()) sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
		return sb.toString().trim().replaceAll(" ", "");
	}
	
	/**
	 * 获取注解中 {@link TypeMirror} 的 {@link TypeElement}
	 *
	 * @param supplier 提供方法
	 * @return {@link TypeElement}
	 */
	static TypeElement getTypeElement(ProcessingEnvironment processingEnv, Supplier<Class<?>> supplier) {
		return (TypeElement) processingEnv.getTypeUtils().asElement(getTypeMirror(supplier));
	}
	
	/**
	 * 获取注解中的 {@link TypeMirror}
	 *
	 * @param supplier 提供方法
	 * @return {@link TypeMirror}
	 */
	static TypeMirror getTypeMirror(Supplier<Class<?>> supplier) {
		TypeMirror typeMirror = null;
		try {
			supplier.get();
		} catch (MirroredTypeException mte) {
			typeMirror = mte.getTypeMirror();
		}
		return typeMirror;
	}
	
	/**
	 * 检测子接口是否声明了相同注释
	 *
	 * @param annotation       注释类型
	 * @param roundEnv         环境
	 * @param processingEnv    处理环境
	 * @param interfaceElement 父接口元素
	 * @return 是否声明了相同注释
	 */
	static boolean checkAnnotation(Class<? extends Annotation> annotation, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv, TypeElement interfaceElement) {
		// 如果没有找到继承的类型，直接返回
		if (interfaceElement == null) return false;
		// 遍历所有元素，检查是否继承了指定类型
		for (Element element : roundEnv.getRootElements()) {
			if (!(element instanceof TypeElement typeElement)) continue;
			// 检查当前元素是否继承了指定类型
			if (!implementsInterface(typeElement, interfaceElement, processingEnv)) continue;
			if (element.getKind() == ElementKind.INTERFACE) {
				if (element.getAnnotation(annotation) != null) continue;
				processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("注册提供器 %s 未实现注解 @%s。", element.getSimpleName(), annotation.getSimpleName()), element);
				return false;
			}
			processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("注册提供器 %s 进行了错误实现！注册提供器只能为接口。", element.getSimpleName()), element);
			return false;
		}
		return true;
	}
	
	/**
	 * 创建类型构建器
	 *
	 * @param type      注册类型
	 * @param superType 父接口类型
	 * @return 预构建的类型构建器
	 */
	static TypeSpec.Builder createTypeBuilder(TypeElement type, Element superType) {
		return TypeSpec.interfaceBuilder(type.getSimpleName() + "Registry")
				.addJavadoc("""
						<h2 style="color:FFC800">{@link $N} 注册表</h2>
						用于注册 {@link $N} 的注册表，由 {@link $N} 构建生成
						\t
						@author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
						@since 1.0.0""", type.getSimpleName(), type.getSimpleName(), superType.getSimpleName())
				.addModifiers(Modifier.PUBLIC)
				.addSuperinterface(superType.asType());
	}
	
	/**
	 * 检查接口是否继承了指定类型
	 *
	 * @param typeElement      子类型元素
	 * @param interfaceElement 父接口元素
	 * @param processingEnv    处理环境
	 * @return 是否继承了类型
	 */
	private static boolean implementsInterface(TypeElement typeElement, TypeElement interfaceElement, ProcessingEnvironment processingEnv) {
		Types typeUtils = processingEnv.getTypeUtils();
		TypeMirror superType = interfaceElement.asType();
		// 检查实现关系
		for (TypeMirror interfaceMirror : typeElement.getInterfaces()) {
			if (typeUtils.isSameType(typeUtils.erasure(interfaceMirror), typeUtils.erasure(superType))) return true;
		}
		return false;
	}
}
