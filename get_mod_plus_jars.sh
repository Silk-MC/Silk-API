#!/bin/bash

# 定义子项目名称
subprojects=(
    "silk-api-base"
    "silk-mod-pass"
    "silk-mod-up"
    "silk-spinning-jenny"
    "silk-rope-stick"
    "silk-magic-cube"
    "silk-spore"
    "silk-generate"
    "silk-pattern"
    "silk-codex"
    "silk-landform"
)

# 删除指定子项目中的 JAR 文件
for subproject in "${subprojects[@]}"; do
    echo "删除 $subproject/build/libs/ 目录下的 JAR 文件..."
    rm -f "$subproject/build/libs/"*.jar
done

# 生成指定子项目的 JAR 文件
for subproject in "${subprojects[@]}"; do
    echo "为 $subproject 生成 JAR 文件..."
    ./gradlew :"$subproject":remapJar
done

# 将所有生成的 JAR 文件复制到 silk-mod-plus/libs 目录中
destination="silk-mod-plus/libs"
echo "复制 JAR 文件到 $destination 目录..."

# 删除目标目录中的所有文件
rm -f "$destination/"*.jar

# 确保目标目录存在
mkdir -p $destination

for subproject in "${subprojects[@]}"; do
    cp "$subproject/build/libs/"*.jar $destination
done

echo "完成所有操作。"