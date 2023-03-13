package cn.stan.utils;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SMSUtils {
    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    public void sendSMS(String phone, String code) {
        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
             * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
             * 你也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
             * 以免泄露密钥对危及你的财产安全。
             * CAM密匙查询获取: https://console.cloud.tencent.com/cam/capi*/
            Credential cred = new Credential(tencentCloudProperties.getSecretId(), tencentCloudProperties.getSecretKey());

            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            // 设置代理（无需要直接忽略）
            // httpProfile.setProxyHost("真实代理ip");
            // httpProfile.setProxyPort(真实代理端口);

            /* SDK默认使用POST方法。
             * 如果你一定要使用GET方法，可以在这里设置。GET方法无法处理一些较大的请求 */
            httpProfile.setReqMethod("POST"); // 默认使用POST

            /* SDK有默认的超时时间，非必要请不要进行调整
             * 如有需要请在代码中查阅以获取最新的默认值 */
            // httpProfile.setConnTimeout(60);

            /* SDK会自动指定域名。通常是不需要特地指定域名的，但是如果你访问的是金融区的服务
             * 则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.tencentcloudapi.com */
            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-nanjing", clientProfile);

            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            req.setSmsSdkAppId("xxxxxx");    // 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId
            req.setSignName("xxxxxx");      // 签名
            req.setTemplateId("xxxxxx");        // 模板ID: 必须填写已审核通过的模板ID,模板ID可登录 [短信控制台] 查看

            /* 下发手机号码，采用 E.164 标准，+[国家或地区码][手机号]
             * 示例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号 */
            String[] phoneNumberSet = {"+86" + phone};//电话号码
            req.setPhoneNumberSet(phoneNumberSet);

            /* 模板参数（自定义占位变量）: 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空 */
            String[] templateParamSet = {code, "5"};
            req.setTemplateParamSet(templateParamSet);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            log.info("-----> 发短信返回: {}", SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error(e.getMessage(), e);
        }
    }

}


