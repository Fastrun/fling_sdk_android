
package com.fireflycast.server.cast.channels;

import com.fireflycast.cast.CastDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;

public class MirroringControlChannel_aum extends CastChannel_avn {
    private final CastDevice a;
    private final String b;
    private long c;
    private String d;
    private String e;

    public MirroringControlChannel_aum(CastDevice castdevice, String s)
    {
        super("urn:x-cast:com.google.cast.webrtc", "MirroringControlChannel");
        c = 0L;
        a = castdevice;
        b = s;
    }

    private String b()
    {
        String s;
        try
        {
            JSONObject jsonobject = new JSONObject(
                    "{                                                                  \"sendFormats\": [                                                {                                                                \"ssrc\": 1,                                                   \"rtp_payload_type\": 127,                                     \"rtp_port\": 2346,                                            \"remote_ssrc\": 2,                                            \"remote_rtp_port\": 2344,                                     \"codec_name\": \"aac_ld_adts\",                               \"index\": 0,                                                  \"rtp_profile\": \"cast\",                                     \"sample_rate\": 48000,                                        \"channels\": 2,                                               \"store_time\": 100,                                           \"bit_rate\": 128000,                                          \"type\": \"audio_source\"                                   },                                                             {                                                                \"ssrc\": 11,                                                  \"rtp_payload_type\": 96,                                      \"rtp_port\": 2346,                                            \"remote_ssrc\": 12,                                           \"remote_rtp_port\": 2344,                                     \"codec_name\": \"h264\",                                      \"index\": 1,                                                  \"max_bit_rate\": 6000000,                                     \"max_frame_rate\": 60,                                        \"resolutions\": [                                                 {                                                                \"height\": 720,                                               \"width\": 1280                                              }                                                            ],                                                           \"rtp_profile\": \"cast\",                                     \"store_time\": 100,                                           \"time_base\": 90000,                                          \"type\": \"video_source\"                                   }                                                           ]                                                          }");
            JSONObject jsonobject1 = jsonobject.getJSONArray("sendFormats").getJSONObject(1);
            jsonobject1.put("aes-key", d);
            jsonobject1.put("aes-iv-mask", e);
            s = jsonobject.toString();
        } catch (JSONException jsonexception)
        {
            mLogs_j.d("Malformed config string; ignoring: %s",
                    new Object[] {
                        "{                                                                  \"sendFormats\": [                                                {                                                                \"ssrc\": 1,                                                   \"rtp_payload_type\": 127,                                     \"rtp_port\": 2346,                                            \"remote_ssrc\": 2,                                            \"remote_rtp_port\": 2344,                                     \"codec_name\": \"aac_ld_adts\",                               \"index\": 0,                                                  \"rtp_profile\": \"cast\",                                     \"sample_rate\": 48000,                                        \"channels\": 2,                                               \"store_time\": 100,                                           \"bit_rate\": 128000,                                          \"type\": \"audio_source\"                                   },                                                             {                                                                \"ssrc\": 11,                                                  \"rtp_payload_type\": 96,                                      \"rtp_port\": 2346,                                            \"remote_ssrc\": 12,                                           \"remote_rtp_port\": 2344,                                     \"codec_name\": \"h264\",                                      \"index\": 1,                                                  \"max_bit_rate\": 6000000,                                     \"max_frame_rate\": 60,                                        \"resolutions\": [                                                 {                                                                \"height\": 720,                                               \"width\": 1280                                              }                                                            ],                                                           \"rtp_profile\": \"cast\",                                     \"store_time\": 100,                                           \"time_base\": 90000,                                          \"type\": \"video_source\"                                   }                                                           ]                                                          }"
                    });
            return "{                                                                  \"sendFormats\": [                                                {                                                                \"ssrc\": 1,                                                   \"rtp_payload_type\": 127,                                     \"rtp_port\": 2346,                                            \"remote_ssrc\": 2,                                            \"remote_rtp_port\": 2344,                                     \"codec_name\": \"aac_ld_adts\",                               \"index\": 0,                                                  \"rtp_profile\": \"cast\",                                     \"sample_rate\": 48000,                                        \"channels\": 2,                                               \"store_time\": 100,                                           \"bit_rate\": 128000,                                          \"type\": \"audio_source\"                                   },                                                             {                                                                \"ssrc\": 11,                                                  \"rtp_payload_type\": 96,                                      \"rtp_port\": 2346,                                            \"remote_ssrc\": 12,                                           \"remote_rtp_port\": 2344,                                     \"codec_name\": \"h264\",                                      \"index\": 1,                                                  \"max_bit_rate\": 6000000,                                     \"max_frame_rate\": 60,                                        \"resolutions\": [                                                 {                                                                \"height\": 720,                                               \"width\": 1280                                              }                                                            ],                                                           \"rtp_profile\": \"cast\",                                     \"store_time\": 100,                                           \"time_base\": 90000,                                          \"type\": \"video_source\"                                   }                                                           ]                                                          }";
        }
        return s;
    }

    private static String e()
    {
        SecureRandom securerandom = new SecureRandom();
        byte abyte0[] = new byte[16];
        securerandom.nextBytes(abyte0);
        StringBuilder stringbuilder = new StringBuilder(32);
        for (int i = 0; i < 16; i++)
        {
            stringbuilder.append(Integer.toHexString(0xf & abyte0[i] >> 4));
            stringbuilder.append(Integer.toHexString(0xf & abyte0[i]));
        }

        return stringbuilder.toString();
    }

    public final void a()
    {
        JSONObject jsonobject = new JSONObject();
        d = e();
        e = e();
        long l = c;
        c = 1L + l;
        try
        {
            jsonobject.put("sessionId", b);
            jsonobject.put("seqNum", l);
            jsonobject.put("type", "OFFER");
            jsonobject.put("codecName", "h264");
            jsonobject.put("aes-key", d);
            jsonobject.put("aes-iv-mask", e);
        } catch (JSONException jsonexception)
        {
            mLogs_j.d("Failed to construct JSONObject for offer!", new Object[0]);
        }
        sendMessage_a(jsonobject.toString(), l, b);
    }

    public final void checkReceivedMessage_a_(String s)
    {
        mLogs_j.d("message received: %s", new Object[] {
                s
        });
        String s1;
        try
        {
            s1 = (new JSONObject(s)).getString("type");
            if (s1.equals("ANSWER"))
            {
                mLogs_j.d("onAnswer", new Object[0]);
                onAnswer_b((new StringBuilder()).append(a.getHostIp_c().getHostAddress())
                        .append(":").append(b()).toString());
                return;
            }
        } catch (JSONException jsonexception)
        {
            mLogs_j.d("Malformed message received; ignoring: %s", new Object[] {
                    s
            });
            return;
        }
        if (s1.equals("STATISTICS"))
        {
            mLogs_j.d("onStatistics", new Object[0]);
            return;
        }
        mLogs_j.d("Bad message!", new Object[0]);
        return;
    }

    protected void onAnswer_b(String s)
    {
    }
}
