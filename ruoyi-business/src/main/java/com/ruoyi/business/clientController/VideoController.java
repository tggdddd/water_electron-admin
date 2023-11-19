package com.ruoyi.business.clientController;

import com.ruoyi.business.tool.OwnImageRemoveWatermarkToolApiConstant;
import com.ruoyi.business.tool.OwnVideoRemoveWatermarkToolApiConstant;
import com.ruoyi.common.annotation.Anonymous;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author laihongfeng
 * @ClassName: VideoController
 * @Description:
 * @date 2023年09月25日 16:32:11
 */
@RequestMapping("/video")
@RestController
public class VideoController {
    /**
     * 万能去水印
     */
    @PostMapping("/universalRemoveWatermark")
    @Anonymous
    public Object universalRemoveWatermark (String url) {
        return OwnVideoRemoveWatermarkToolApiConstant.removeWatermark(url);
    }

    /**
     * 万能图集去水印
     */
    @PostMapping("/universalImageRemoveWatermark")
    @Anonymous
    public Object universalImageRemoveWatermark (String url) {
        return OwnImageRemoveWatermarkToolApiConstant.removeWatermark(url);
    }
}
