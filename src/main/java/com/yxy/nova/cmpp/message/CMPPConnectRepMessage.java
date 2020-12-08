// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-11 17:43:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CMPPConnectRepMessage.java

package com.yxy.nova.cmpp.message;



// Referenced classes of package com.huawei.insa2.comm.cmpp.message:
//            CMPPMessage

import com.yxy.nova.cmpp.CMPPConstant;
import com.yxy.nova.cmpp.common.TypeConvert;
import com.google.common.base.Charsets;

public class CMPPConnectRepMessage extends CMPPMessage
{

    public CMPPConnectRepMessage(byte buf[])
        throws IllegalArgumentException
    {
        super.buf = new byte[22];
        if(buf.length != 22)
        {
            throw new IllegalArgumentException(CMPPConstant.SMC_MESSAGE_ERROR);
        } else
        {
            System.arraycopy(buf, 0, super.buf, 0, buf.length);
            super.sequence_Id = TypeConvert.byte2int(super.buf, 0);
            return;
        }
    }

    public byte getStatus()
    {
        return super.buf[4];
    }

    public byte[] getAuthenticatorISMG()
    {
        byte tmpbuf[] = new byte[16];
        System.arraycopy(super.buf, 5, tmpbuf, 0, 16);
        return tmpbuf;
    }

    public byte getVersion()
    {
        return super.buf[21];
    }

    public String toString()
    {
        String tmpStr = "CMPP_Connect_REP: ";
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append("Sequence_Id=").append(getSequenceId())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Status=").append(getStatus())));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",AuthenticatorISMG=").append(new String(getAuthenticatorISMG(), Charsets.UTF_8))));
        tmpStr = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(tmpStr)))).append(",Version=").append(getVersion())));
        return tmpStr;
    }

    public int getCommandId()
    {
        return 0x80000001;
    }
}