
package tv.matchstick.server.fling.socket.data;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

public final class C_axm extends C_igu {
    private boolean a;
    private int protocolVersion;
    private boolean c;
    private String sourceId;
    private boolean e;
    private String destinationId;
    private boolean g;
    private String namespace_p;
    private boolean i;

    // 0 is STARING, return payloadUtf8
    // 1 is BINARY, return payloadBinary
    private int payloadType;
    private boolean k;
    private String payloadUtf8;
    private boolean m;
    private BinaryPayload payloadBinary;
    private int mTotalLen;

    public C_axm()
    {
        protocolVersion = 0;
        sourceId = "";
        destinationId = "";
        namespace_p = "";
        payloadType = 0;
        payloadUtf8 = "";
        payloadBinary = BinaryPayload.mInstance;
        mTotalLen = -1;
    }

    public String payload() {
        return "{\n"
                + "  protocolVersion: " + protocolVersion + ",\n"
                + "  source_id:" + sourceId + ",\n"
                + "  destination_id:" + destinationId + ",\n"
                + "  namespace:" + namespace_p + ",\n"
                + "  payload_type:" + payloadType + ",\n"
                + "  payload_utf8:" + payloadUtf8 + "\n"
                + "}";
    }
    
    public JSONObject buildJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("protocolVersion", protocolVersion);
            obj.put("sourceId", sourceId);
            obj.put("destinationId", destinationId);
            obj.put("namespace", namespace_p);
            if (payloadType == 0) {
                obj.put("payloadType", "STRING");
                obj.put("payloadUtf8", payloadUtf8);
            } else {
                obj.put("payloadType", "BINARY");
                obj.put("payloadBinary", Base64.encode(payloadBinary.b(), Base64.DEFAULT));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
    
    public void parseJson(String json) {
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            if (obj.has("protocolVersion"))
                protocolVersion(obj.optInt("protocolVersion"));
            if (obj.has("sourceId"))
                sourceId(obj.optString("sourceId"));
            if (obj.has("destinationId"))
                destinationId(obj.optString("destinationId"));
            if (obj.has("namespace"))
                namespace(obj.optString("namespace"));
            String type = null;
            if (obj.has("payloadType"))
                type = obj.optString("payloadType");
            if (type.equals("STRING")) {
                payloadType(0);
                payloadMessage(obj.optString("payloadUtf8"));
            } else {
                payloadType(1);
                payloadBinary(BinaryPayload.a(Base64.decode(obj.optString("payloadBinary"), Base64.DEFAULT)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       
    }

    public static C_axm a(byte abyte0[])
    {
        return (C_axm) (new C_axm()).b(abyte0);
    }

    public final C_axm protocolVersion(int version)
    {
        a = true;
        protocolVersion = version;
        return this;
    }

    public final C_axm payloadBinary(BinaryPayload igp1)
    {
        m = true;
        payloadBinary = igp1;
        return this;
    }

    public final C_axm sourceId(String s)
    {
        c = true;
        sourceId = s;
        return this;
    }

    public final C_igu a(C_igq igq1) throws IOException
    {
        do
        {
            int i1 = igq1.a();
            switch (i1)
            {
                default:
                    if (igq1.b(i1))
                        continue;
                    // fall through

                case 0: // '\0'
                    return this;

                case 8: // '\b'
                    protocolVersion(igq1.h());
                    break;

                case 18: // '\022'
                    sourceId(igq1.e());
                    break;

                case 26: // '\032'
                    destinationId(igq1.e());
                    break;

                case 34: // '"'
                    namespace(igq1.e());
                    break;

                case 40: // '('
                    payloadType(igq1.h());
                    break;

                case 50: // '2'
                    payloadMessage(igq1.e());
                    break;

                case 58: // ':'
                    payloadBinary(igq1.f());
                    break;
            }
        } while (true);
    }

    public final String getNamespace()
    {
        return namespace_p;
    }

    public final void a(C_igr igr1) throws IOException
    {
        if (a)
            igr1.a(1, protocolVersion);
        if (c)
            igr1.a(2, sourceId);
        if (e)
            igr1.a(3, destinationId);
        if (g)
            igr1.a(4, namespace_p);
        if (i)
            igr1.a(5, payloadType);
        if (k)
            igr1.a(6, payloadUtf8);
        if (m)
            igr1.a(7, payloadBinary);
    }

    public final int b()
    {
        if (mTotalLen < 0)
            c();
        return mTotalLen;
    }

    public final C_axm payloadType(int i1)
    {
        i = true;
        payloadType = i1;
        return this;
    }

    public final C_axm destinationId(String transId)
    {
        e = true;
        destinationId = transId;
        return this;
    }

    public final int c()
    {
        boolean flag = a;
        int i1 = 0;
        if (flag)
            i1 = 0 + C_igr.d(1, protocolVersion);
        if (c)
            i1 += C_igr.b(2, sourceId);
        if (e)
            i1 += C_igr.b(3, destinationId);
        if (g)
            i1 += C_igr.b(4, namespace_p);
        if (i)
            i1 += C_igr.d(5, payloadType);
        if (k)
            i1 += C_igr.b(6, payloadUtf8);
        if (m)
            i1 += C_igr.b(7, payloadBinary);
        mTotalLen = i1;
        return i1;
    }

    public final C_axm namespace(String namespace)
    {
        g = true;
        namespace_p = namespace;
        return this;
    }

    public final int getPayloadType()
    {
        return payloadType;
    }

    public final C_axm payloadMessage(String message)
    {
        k = true;
        payloadUtf8 = message;
        return this;
    }

    public final String getMessage()
    {
        return payloadUtf8;
    }

    public final BinaryPayload getBinaryMessage()
    {
        return payloadBinary;
    }
}
