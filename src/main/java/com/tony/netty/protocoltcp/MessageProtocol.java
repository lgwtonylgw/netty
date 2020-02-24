package com.tony.netty.protocoltcp;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;

/**
 * Created on 2020/2/24 22:37
 *
 * @author Tony
 * @projectName NettyPro
 */
@Data
public class MessageProtocol {
    private int len;
    private byte[] content;
}
