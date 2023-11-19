package com.ruoyi.business.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailBuilder implements Serializable {
    /**发件人*/
    private String from;

    /**
     * 邮件接收方，可多人
     */
    private String[] tos;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
}

