package com.wtk.xiaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import com.wtk.xiaicodemother.ai.model.HtmlCodeResult;
import com.wtk.xiaicodemother.exception.BusinessException;
import com.wtk.xiaicodemother.exception.ErrorCode;
import com.wtk.xiaicodemother.model.enums.CodeGenTypeEnum;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML代码内容不能为空");
        }
    }
}
