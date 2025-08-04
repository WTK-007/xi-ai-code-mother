package com.wtk.xiaicodemother.ai;

public interface AiCodeGeneratorService {

    /**
     * 生成代码
     * @param userMessage 用户提示词
     * @return AI 的输出结果
     */
    String generateCode(String userMessage);
}
