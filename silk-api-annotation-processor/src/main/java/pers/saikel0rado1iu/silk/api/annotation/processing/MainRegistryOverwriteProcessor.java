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
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.annotation.MainRegistryOverwrite;
import pers.saikel0rado1iu.silk.api.annotation.Register;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pers.saikel0rado1iu.silk.api.annotation.processing.ProcessorUtil.getTypeElement;

/**
 * <h2 style="color:FFC800">主要注册表覆写处理器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("pers.saikel0rado1iu.silk.api.annotation.MainRegistryOverwrite")
public final class MainRegistryOverwriteProcessor extends AbstractProcessor {
	private static final String IO_ERROR = "出现 I/O 错误：无法找到 %s 的字节码或无法读取 class 文件";
	private static final String REGISTRAR_ERROR = "注册器参数错误：%s 并不是一个实现了注册器的接口或类";
	private static final String FIELD_NOT_FIND = "字段未找到：%s 中并未声明一个名为 %s 的静态字段";
	private static final String METHOD_NOT_FIND = "方法未找到：%s 中并未在静态初始化代码块中调用名为 %s 的方法";
	private static final String FIELD_NOT_USE_METHOD = "字段声明未使用方法：字段 %s 的注册过程中并未调用名为 %s 的方法";
	private static final String OVERWRITE_NOT_FIND = "覆盖字段未找到：覆盖字段 %s 并与字段 %s 在同个类或接口中声明";
	
	private static String getParameterDesc(Filer filer, TypeElement registrar, String methodName) {
		String className = registrar.getQualifiedName().toString().replace('.', '/') + ".class";
		
		try (InputStream classStream = filer.getResource(StandardLocation.CLASS_PATH, "", className).openInputStream()) {
			ClassReader classReader = new ClassReader(classStream);
			ClassNode classNode = new ClassNode();
			classReader.accept(classNode, 0);
			// 遍历所有方法
			for (MethodNode method : classNode.methods) {
				if (!"<clinit>".equals(method.name)) continue;
				// 只关注类初始化方法
				AbstractInsnNode[] instructions = method.instructions.toArray();
				for (AbstractInsnNode node : instructions) {
					if (!(node instanceof MethodInsnNode m) || !m.name.equals(methodName)) continue;
					Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(m.desc);
					if (matcher.find()) return matcher.group(1);
					else throw new MissingFormatArgumentException(String.format("方法格式错误：方法 %s 的描述符格式不正确，请检查方法参数", methodName));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format(IO_ERROR, registrar), e);
		}
		throw new IllegalArgumentException(String.format(METHOD_NOT_FIND, registrar, methodName));
	}
	
	private static int findMethodOrdinal(Filer filer, TypeElement registrar, String fieldName, String methodName) {
		String className = registrar.getQualifiedName().toString().replace('.', '/') + ".class";
		
		try (InputStream classStream = filer.getResource(StandardLocation.CLASS_PATH, "", className).openInputStream()) {
			ClassReader classReader = new ClassReader(classStream);
			ClassNode classNode = new ClassNode();
			classReader.accept(classNode, 0);
			int globalCount = 0;
			int ordinal = -1;
			// 遍历所有方法
			boolean haveField = false;
			for (MethodNode method : classNode.methods) {
				if (!"<clinit>".equals(method.name)) continue;
				// 只关注类初始化方法
				AbstractInsnNode[] instructions = method.instructions.toArray();
				for (int count = 0; count < instructions.length; count++) {
					AbstractInsnNode node = instructions[count];
					// 查找 TEST_ITEM1 的赋值
					if (node instanceof FieldInsnNode field && fieldName.equals(field.name)) {
						haveField = true;
						// 向上查找 group 方法调用的序号
						for (int i = count; i >= 0; i--) {
							AbstractInsnNode prev = instructions[i];
							if (!(prev instanceof MethodInsnNode m)) continue;
							if (!methodName.equals(m.name)) continue;
							ordinal = globalCount;
							break;
						}
					}
					// 统计全局 group 方法的调用
					if (node instanceof MethodInsnNode m && m.name.equals(methodName)) globalCount++;
				}
			}
			if (!haveField) throw new IllegalArgumentException(String.format(FIELD_NOT_FIND, registrar, fieldName));
			// 未找到字段调用的方法
			if (ordinal == -1) throw new IllegalArgumentException(String.format(FIELD_NOT_USE_METHOD, fieldName, methodName));
			return ordinal - 1;
		} catch (IOException e) {
			throw new RuntimeException(String.format(IO_ERROR, registrar), e);
		}
	}
	
	private static int findFieldOrdinal(Filer filer, TypeElement registrar, String fieldName) {
		String className = registrar.getQualifiedName().toString().replace('.', '/') + ".class";
		
		try (InputStream classStream = filer.getResource(StandardLocation.CLASS_PATH, "", className).openInputStream()) {
			ClassReader classReader = new ClassReader(classStream);
			ClassNode classNode = new ClassNode();
			classReader.accept(classNode, 0);
			int ordinal = 0;
			for (FieldNode field : classNode.fields) {
				if (fieldName.equals(field.name)) return ordinal;
				ordinal++;
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format(IO_ERROR, registrar), e);
		}
		throw new IllegalArgumentException(String.format(FIELD_NOT_FIND, registrar, fieldName));
	}
	
	static TypeSpec.Builder generateRegister(TypeSpec.Builder builder, Element element, ProcessingEnvironment processingEnv, TypeElement registrar, String field, Register register) {
		if ("register".equals(register.method())) throw new IllegalArgumentException("非法方法：用户永远也不应该尝试修改 register() 注册方法的注册 ID");
		List<? extends TypeMirror> interfaces = registrar.getInterfaces();
		if (interfaces.isEmpty()) throw new IllegalArgumentException(String.format(REGISTRAR_ERROR, registrar));
		TypeMirror parameter = null;
		for (Element e : element.getEnclosingElement().getEnclosedElements()) {
			if (e.getKind() != ElementKind.FIELD) continue;
			VariableElement variableElement = (VariableElement) e;
			// 检查字段名称是否匹配
			if (!variableElement.getSimpleName().toString().equals(register.overwrite())) continue;
			parameter = variableElement.asType();
		}
		if (parameter == null) throw new IllegalArgumentException(String.format(OVERWRITE_NOT_FIND, register.overwrite(), field));
		String prefix = ((TypeElement) processingEnv.getTypeUtils().asElement(interfaces.getFirst())).getQualifiedName().toString().replaceAll("\\.", "/").replaceAll("Registry", "");
		TypeVariableName t = TypeVariableName.get("T");
		TypeName type = TypeName.get(parameter);
		String targetFormat = "other".equals(register.method())
				? "L%1$sRegistrationProvider$MainRegistrar;%2$s(%3$s)L pers/saikel0rado1iu/silk/api/modpass/registry/MainRegistrationProvider$Registrar;"
				: "L%1$sRegistrationProvider$MainRegistrar;%2$s(%3$s)L%1$sRegistrationProvider$MainRegistrar;";
		MethodSpec method = MethodSpec.methodBuilder(register.method())
				.addJavadoc("""
						此混入方法由 {@link pers.saikel0rado1iu.silk.api.annotation.processing.MainRegistryOverwriteProcessor} 自动生成<br>
						用于修改注册项的其他注册方法
						""")
				.addAnnotation(AnnotationSpec.builder(ModifyArg.class)
						.addMember("method", "$S", "<clinit>")
						.addMember("at", "$L", AnnotationSpec.builder(At.class)
								.addMember("value", "$S", "INVOKE")
								.addMember("target", "$S", String.format(targetFormat, prefix, register.method(), getParameterDesc(processingEnv.getFiler(), registrar, register.method())))
								.addMember("ordinal", "$L", findMethodOrdinal(processingEnv.getFiler(), registrar, field, register.method()))
								.build())
						.addMember("remap", "$L", register.remap())
						.build())
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.addTypeVariable(t)
				.addParameter(type, "type")
				.addCode(CodeBlock.builder()
						.addStatement("return $L.$L", element.getEnclosingElement(), register.overwrite())
						.build())
				.returns(type)
				.build();
		return builder.addMethod(method);
	}
	
	static TypeSpec.Builder generateOverwrite(TypeSpec.Builder builder, Element element, ProcessingEnvironment processingEnv, TypeElement registrar, String field) {
		List<? extends TypeMirror> interfaces = registrar.getInterfaces();
		if (interfaces.isEmpty()) throw new IllegalArgumentException(String.format(REGISTRAR_ERROR, registrar));
		String target = ((TypeElement) processingEnv.getTypeUtils().asElement(interfaces.getFirst())).getQualifiedName().toString().replaceAll("\\.", "/").replaceAll("Registry", "");
		TypeVariableName t = TypeVariableName.get("T");
		ParameterizedTypeName supplier = ParameterizedTypeName.get(ClassName.get(Supplier.class), t);
		MethodSpec overwrite = MethodSpec.methodBuilder("overwrite")
				.addJavadoc("""
						此混入方法由 {@link pers.saikel0rado1iu.silk.api.annotation.processing.MainRegistryOverwriteProcessor} 自动生成<br>
						用于修改注册项注册的实例
						""")
				.addAnnotation(AnnotationSpec.builder(ModifyArg.class)
						.addMember("method", "$S", "<clinit>")
						.addMember("at", "$L", AnnotationSpec.builder(At.class)
								.addMember("value", "$S", "INVOKE")
								.addMember("target", "$S", String.format("L%1$sRegistry;registrar(Ljava/util/function/Supplier;)L%1$sRegistrationProvider$MainRegistrar;", target))
								.addMember("ordinal", "$L", findFieldOrdinal(processingEnv.getFiler(), registrar, field))
								.build())
						.addMember("remap", "$L", false)
						.build())
				.addModifiers(Modifier.PRIVATE, Modifier.STATIC)
				.addTypeVariable(t)
				.addParameter(supplier, "type")
				.addCode(CodeBlock.builder()
						.addStatement("return () -> (T) $L.$L", element.getEnclosingElement(), element.getSimpleName())
						.build())
				.returns(supplier)
				.build();
		return builder.addMethod(overwrite);
	}
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(MainRegistryOverwrite.class)) {
			MainRegistryOverwrite mainRegistryOverwrite = element.getAnnotation(MainRegistryOverwrite.class);
			String pack = mainRegistryOverwrite.pack();
			TypeElement registrar = getTypeElement(processingEnv, mainRegistryOverwrite::registrar);
			String field = mainRegistryOverwrite.field();
			Register[] registers = mainRegistryOverwrite.registers();
			String className = ProcessorUtil.capitalize(field) + "MainMixin";
			TypeSpec.Builder builder = TypeSpec.interfaceBuilder(className)
					.addJavadoc(String.format("""
							此混入类由 {@link pers.saikel0rado1iu.silk.api.annotation.processing.MainRegistryOverwriteProcessor} 自动生成<br>
							由 {@link %s#%s} 调用 {@link pers.saikel0rado1iu.silk.api.annotation.MainRegistryOverwrite} 生成
							""", element.getEnclosingElement().asType().toString(), element.getSimpleName()))
					.addAnnotation(AnnotationSpec.builder(Mixin.class).addMember("value", "$T.class", registrar).build());
			builder = generateOverwrite(builder, element, processingEnv, registrar, field);
			for (Register register : registers) builder = generateRegister(builder, element, processingEnv, registrar, field, register);
			TypeSpec typeSpec = builder.build();
			// 创建一个文件
			try {
				FileObject existingFile = processingEnv.getFiler().getResource(StandardLocation.SOURCE_OUTPUT, pack, className + ".java");
				if (existingFile != null && existingFile.getLastModified() > 0) return true;
				JavaFile javaFile = JavaFile.builder(pack, typeSpec).build();
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
