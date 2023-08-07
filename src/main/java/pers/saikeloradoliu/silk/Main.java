package pers.saikeloradoliu.silk;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    /**
     * <p>此记录器用于将文本写入控制台和日志文件。</p>
     * <p>使用您的模组 ID 作为记录器的名称被认为是最佳实践。</p>
     * <p>这样，就很清楚是哪个模组写了信息、警告和错误。</p>
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    /**
     * <p>只要 Minecraft 处于 mod-load-ready(模组-加载-准备) 状态, 此代码就会运行.</p>
     * <p>但是, 有些东西（比如资源）可能仍然未初始化.</p>
     * <p style="color:DD0000">!谨慎操作!</p>
     */
    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
    }
}