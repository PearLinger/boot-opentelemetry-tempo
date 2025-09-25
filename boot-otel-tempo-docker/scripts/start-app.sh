#!/bin/bash

# 1. 为含空格/多参数的变量添加引号，避免解析错误
[ -z "$MIN_HEAP_SIZE" ] && MIN_HEAP_SIZE="40M"
[ -z "$MAX_HEAP_SIZE" ] && MAX_HEAP_SIZE="512M"
[ -z "$THREADSTACK_SIZE" ] && THREADSTACK_SIZE="228k"
# 关键修正：用引号包裹多参数的 JAVA_GC_ARGS
[ -z "$JAVA_GC_ARGS" ] && JAVA_GC_ARGS="-XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:+UseSerialGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90"
[ -z "$PROG_ARGS" ] && PROG_ARGS=""

set -e

# 2. 检查 APP_NAME 是否定义（避免 jar 路径无效）
if [ -z "$APP_NAME" ]; then
  echo "Error: APP_NAME environment variable is not set!"
  exit 1
fi

# 3. 验证 jar 文件是否存在（使用定义的 APP_NAME）
if [ ! -f "${APP_HOME}/${APP_NAME}.jar" ]; then
  echo "Springboot jar '${APP_HOME}/${APP_NAME}.jar' not found! Exiting..."
  exit 1
fi

# 4. 打印参数（可选，用于调试）
echo "Java options: $JAVA_OPTS -Xms${MIN_HEAP_SIZE} -Xmx${MAX_HEAP_SIZE} -Xss${THREADSTACK_SIZE} $JAVA_GC_ARGS $JAVA_DIAG_ARGS $JAVA_OPTS_APPEND"
SPRING_PROFILES_ACTIVE=dev

# 5. 启动命令（保持参数正确拼接）
java $JAVA_OPTS \
  -Xms${MIN_HEAP_SIZE} \
  -Xmx${MAX_HEAP_SIZE} \
  -Xss${THREADSTACK_SIZE} \
  $JAVA_GC_ARGS \
  $JAVA_DIAG_ARGS \
  $JAVA_OPTS_APPEND \
  -Dspring.profiles.active="$SPRING_PROFILES_ACTIVE" \
  -jar "${APP_HOME}/${APP_NAME}.jar" \
  $PROG_ARGS