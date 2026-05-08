# 机械动力：锋楪扩展 / Create Extend

一个基于 [Create](https://createmod.net/) 的 Minecraft NeoForge 扩展模组，由 [Frontleaves](https://github.com/Frontleaves) 团队开发。

## 功能

- 通过 [Curios](https://www.curseforge.com/minecraft/mc-mods/curios) 或 [Accessories](https://modrinth.com/mod/accessories) 饰品栏佩戴的飞行员护目镜（Aviator's Goggles）与创建护目镜（Create Goggles）可正确触发 Create 的工程师护目镜功能（如显示动力学信息等）。
- 兼容 Curios 与 Accessories 两套饰品系统，按实际安装情况自动适配。

## 依赖

| 模组 | 类型 | 版本要求 |
|------|------|---------|
| Minecraft | 必需 | 1.21.1 |
| NeoForge | 必需 | ≥21 |
| Create | 必需 | ≥6.0.10 |
| Aeronautics | 必需 | ≥1.0 |
| Curios | 可选 | ≥1.0 |
| Accessories | 可选 | ≥1.0 |

## 构建

本项目使用 Gradle 构建，需要 JDK 21。

```bash
./gradlew build
```

构建产物位于 `build/libs/` 目录下。

## 运行

```bash
# 启动客户端
./gradlew runClient

# 启动服务端
./gradlew runServer
```

## 许可证

本项目基于 [MIT License](LICENSE) 开源。

## 作者

- [筱锋 (xiao_lfeng)](https://github.com/xiao-lfeng)
