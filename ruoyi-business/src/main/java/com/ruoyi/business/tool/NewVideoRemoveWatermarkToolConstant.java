package com.ruoyi.business.tool;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.watermark.utils.BogusUtils;
import com.ruoyi.business.exception.Biz;
import com.ruoyi.business.exception.CustomerException;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.http.HttpHeaders;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewVideoRemoveWatermarkToolConstant {
    /**
     * 快手无水印
     *
     * @param url
     * @return
     */
    public static JSONObject kuaishou(String url) {
        JSONObject result = new JSONObject();
        HttpResponse response = HttpRequest.get(BogusUtils.commGetUrl(url))
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .header(Header.COOKIE, "did=web_896aeb3b8a0110f39c7bf7f250f49306; didv=1700292596698; kpf=PC_WEB; clientid=3; kpn=KUAISHOU_VISION")
                .execute();
        String location = response.header("Location");
        HttpResponse realResponse = HttpRequest.get(location)
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .header(Header.COOKIE, "did=web_896aeb3b8a0110f39c7bf7f250f49306; didv=1700292596698; kpf=PC_WEB; clientid=3; kpn=KUAISHOU_VISION")
                .header(Header.REFERER, url)
                .execute();
        String body = realResponse.body();
        Pattern r = Pattern.compile("window.__APOLLO_STATE__\b*?=(.*?})\b*?;");
        Matcher m = r.matcher(body);
        if (m.find()) {
            String json = m.group(1);
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject defaultClient = jsonObject.getJSONObject("defaultClient");
            Set<String> strings = defaultClient.keySet();
            strings.forEach(e -> {
                if (e.startsWith("VisionVideoDetailAuthor")) {
                    JSONObject author = defaultClient.getJSONObject(e);
                    result.put("author", author.getString("name"));
                    result.put("uid", author.getString("id"));
                    result.put("avatar", author.get("headerUrl"));
                    result.put("sec_uid", author.getString("id"));
                }
                if (e.startsWith("VisionVideoDetailPhoto")) {
                    JSONObject video = defaultClient.getJSONObject(e);
                    result.put("duration", video.getLongValue("duration") / 1000);
                    result.put("time", video.get("timestamp"));
                    result.put("title", video.get("caption"));
                    result.put("cover", video.get("coverUrl"));
                    result.put("analysisPlatform", "快手");

                    JSONObject videoResource = video.getJSONObject("videoResource");
                    JSONObject json1 = videoResource.getJSONObject("json");
                    JSONObject jsonObject1 = json1.getJSONObject("hevc");
                    JSONObject adaptationSet = jsonObject1.getJSONArray("adaptationSet").getJSONObject(0);
                    JSONObject representation = adaptationSet.getJSONArray("representation").getJSONObject(0);
                    result.put("url", representation.getString("url"));
                    result.put("music", "{}");
                }
            });
            return result;
        }
        throw new CustomerException("");
    }

    /**
     * 快手主页解析
     *
     * @param uid
     * @return
     */
    public static JSONObject kuaishouHost(String uid) {
        JSONObject result = new JSONObject();
        HttpResponse execute = HttpRequest.post("https://www.kuaishou.com/graphql")
                .header(Header.REFERER, "https://www.kuaishou.com/profile/" + uid)
                .header(Header.UPGRADE, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0")
                .header(Header.COOKIE, "did=web_896aeb3b8a0110f39c7bf7f250f49306; didv=1700292596698; kpf=PC_WEB; clientid=3; kpn=KUAISHOU_VISION")
                .body(
                        String.format("{\n" +
                                "    \"operationName\": \"visionProfilePhotoList\",\n" +
                                "    \"variables\": {\n" +
                                "        \"userId\": \"%s\",\n" +
                                "        \"pcursor\": \"\",\n" +
                                "        \"page\": \"profile\"\n" +
                                "    },\n" +
                                "    \"query\": \"fragment photoContent on PhotoEntity {\\n  __typename\\n  id\\n  duration\\n  caption\\n  originCaption\\n  likeCount\\n  viewCount\\n  commentCount\\n  realLikeCount\\n  coverUrl\\n  photoUrl\\n  photoH265Url\\n  manifest\\n  manifestH265\\n  videoResource\\n  coverUrls {\\n    url\\n    __typename\\n  }\\n  timestamp\\n  expTag\\n  animatedCoverUrl\\n  distance\\n  videoRatio\\n  liked\\n  stereoType\\n  profileUserTopPhoto\\n  musicBlocked\\n}\\n\\nfragment recoPhotoFragment on recoPhotoEntity {\\n  __typename\\n  id\\n  duration\\n  caption\\n  originCaption\\n  likeCount\\n  viewCount\\n  commentCount\\n  realLikeCount\\n  coverUrl\\n  photoUrl\\n  photoH265Url\\n  manifest\\n  manifestH265\\n  videoResource\\n  coverUrls {\\n    url\\n    __typename\\n  }\\n  timestamp\\n  expTag\\n  animatedCoverUrl\\n  distance\\n  videoRatio\\n  liked\\n  stereoType\\n  profileUserTopPhoto\\n  musicBlocked\\n}\\n\\nfragment feedContent on Feed {\\n  type\\n  author {\\n    id\\n    name\\n    headerUrl\\n    following\\n    headerUrls {\\n      url\\n      __typename\\n    }\\n    __typename\\n  }\\n  photo {\\n    ...photoContent\\n    ...recoPhotoFragment\\n    __typename\\n  }\\n  canAddComment\\n  llsid\\n  status\\n  currentPcursor\\n  tags {\\n    type\\n    name\\n    __typename\\n  }\\n  __typename\\n}\\n\\nquery visionProfilePhotoList($pcursor: String, $userId: String, $page: String, $webPageArea: String) {\\n  visionProfilePhotoList(pcursor: $pcursor, userId: $userId, page: $page, webPageArea: $webPageArea) {\\n    result\\n    llsid\\n    webPageArea\\n    feeds {\\n      ...feedContent\\n      __typename\\n    }\\n    hostName\\n    pcursor\\n    __typename\\n  }\\n}\\n\"\n" +
                                "}", uid)
                ).execute();
        String body = execute.body();
        JSONObject parse = JSON.parseObject(body);
        JSONArray jsonArray = parse.getJSONObject("data").getJSONObject("visionProfilePhotoList").getJSONArray("feeds");
        if (!jsonArray.isEmpty()) {
            JSONArray rows = new JSONArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = new JSONObject();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                item.put("desc", jsonObject.getJSONObject("photo").getString("caption"));
                item.put("duration", jsonObject.getJSONObject("photo").getLongValue("duration") / 1000);
                item.put("cover", jsonObject.getJSONObject("photo").getString("coverUrl"));
                item.put("time", jsonObject.getJSONObject("photo").getString("timestamp"));
                item.put("url", jsonObject.getJSONObject("photo").getJSONObject("videoResource")
                        .getJSONObject("h264").getJSONArray("adaptationSet")
                        .getJSONObject(0).getJSONArray("representation")
                        .getJSONObject(0).getString("url"));
                JSONObject itemWrap = new JSONObject();
                itemWrap.put("video", item);
                rows.add(itemWrap);
                if (i == 0) {
                    result.put("author", jsonObject.getJSONObject("author").getString("name"));
                    result.put("avatar", jsonObject.getJSONObject("author").getString("headerUrl"));
                    result.put("minCursor", -1);
                    result.put("maxCursor", -1);
                    result.put("uid", jsonObject.getJSONObject("author").getString("id"));
                }
            }
            result.put("rowsList", rows);
        }
        return result;
    }

    /**
     * 西瓜主页解析
     */
    public static JSONObject xiguaHost(String uid, Integer page) {
        Integer offset = (page - 1) * 30;
        String body = HttpRequest
                .get(String.format("https://www.ixigua.com/api/videov2/author/new_video_list?to_user_id=%s&offset=%d&limit=30", uid, offset))
                .header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54")
                .header(Header.COOKIE, "MONITOR_WEB_ID=7892c49b-296e-4499-8704-e47c1b150c18; ixigua-a-s=1; ttcid=af99669b6304453480454f150701d5c226; BD_REF=1; __ac_nonce=060d88ff000a75e8d17eb; __ac_signature=_02B4Z6wo00f01kX9ZpgAAIDAKIBBQUIPYT5F2WIAAPG2ad; ttwid=1%7CcIsVF_3vqSIk4XErhPB0H2VaTxT0tdsTMRbMjrJOPN8%7C1624806049%7C08ce7dd6f7d20506a41ba0a331ef96a6505d96731e6ad9f6c8c709f53f227ab1")
                .header(Header.REFERER, String.format("https://www.ixigua.com/home/%s/?source=pgc_author_name&list_entrance=anyVideo", uid))
                .execute().body();
        JSONObject bodyJson = JSONObject.parseObject(body);
        JSONObject data = bodyJson.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("videoList");
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject item = new JSONObject();
            item.put("desc", jsonObject.getString("title"));
            item.put("duration", jsonObject.getString("video_duration"));
            item.put("cover", jsonObject.getJSONObject("first_frame_image").getString("url"));
            item.put("video_id", jsonObject.getString("item_id"));
            item.put("time", jsonObject.getString("publish_time"));
            item.put("url", jsonObject.getString("item_id"));
            JSONObject itemWrap = new JSONObject();
            itemWrap.put("video", item);
            rows.add(itemWrap);
            if (i == 0) {
                result.put("author", jsonObject.getJSONObject("user_info").getString("name"));
                result.put("avatar", jsonObject.getJSONObject("user_info").getString("avatar_url"));
                result.put("minCursor", -1);
                result.put("maxCursor", page + 1);
                result.put("uid", jsonObject.getJSONObject("user_info").getString("user_id"));
            }
        }
        result.put("rowsList", rows);
        return result;
    }

    /**
     * 西瓜下载链接
     */
    public static String xiguaUrlById(String videoId) {
        JSONObject videoVo = new JSONObject();
        String kuaishouDetail = HttpRequest.get("https://www.ixigua.com/" + videoId).header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.54").header(Header.COOKIE, "MONITOR_WEB_ID=7892c49b-296e-4499-8704-e47c1b150c18; ixigua-a-s=1; ttcid=af99669b6304453480454f150701d5c226; BD_REF=1; __ac_nonce=060d88ff000a75e8d17eb; __ac_signature=_02B4Z6wo00f01kX9ZpgAAIDAKIBBQUIPYT5F2WIAAPG2ad; ttwid=1%7CcIsVF_3vqSIk4XErhPB0H2VaTxT0tdsTMRbMjrJOPN8%7C1624806049%7C08ce7dd6f7d20506a41ba0a331ef96a6505d96731e6ad9f6c8c709f53f227ab1").execute().body();
        String data = BogusUtils.commGetVideoId("window._SSR_HYDRATED_DATA=(.*?)</script>", kuaishouDetail).replaceAll("undefined", "null");
        cn.hutool.json.JSONObject bean = JSONUtil.toBean(data, cn.hutool.json.JSONObject.class);
        cn.hutool.json.JSONObject detailData = bean.getJSONObject("anyVideo").getJSONObject("gidInformation").getJSONObject("packerData").getJSONObject("video");
        String videoUrl;
        cn.hutool.json.JSONObject jsonObject = detailData.getJSONObject("videoResource");
        if(jsonObject.getJSONObject("dash_120fps")!=null) {
            String urlEncode = jsonObject
                    .getJSONObject("dash_120fps")
                    .getJSONObject("dynamic_video")
                    .getJSONArray("dynamic_video_list")
                    .getJSONObject(1)
                    .getStr("backup_url_1");
            videoUrl = Base64.decodeStr(urlEncode);
        }else{
            cn.hutool.json.JSONObject videoList = jsonObject
                    .getJSONObject("h265").getJSONObject("normal")
                    .getJSONObject("video_list");
            if(videoList.getJSONObject("video_4")!=null){
                videoUrl = Base64.decodeStr(videoList.getJSONObject("video_4").getStr("backup_url_1"));
            }else if(videoList.getJSONObject("video_3")!=null){
                videoUrl = Base64.decodeStr(videoList.getJSONObject("video_3").getStr("backup_url_1"));
            }else if(videoList.getJSONObject("video_2")!=null){
                videoUrl = Base64.decodeStr(videoList.getJSONObject("video_2").getStr("backup_url_1"));
            }else {
                videoUrl = Base64.decodeStr(videoList.getJSONObject("video_1").getStr("backup_url_1"));
            }
        }
        return videoUrl;
    }
    /**
     * B站下载视频
     */
    public static String bilibiliUrlById(String videoId){
        String cidData = HttpRequest.get("https://api.bilibili.com/x/player/pagelist?bvid=" + videoId).header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header(Header.REFERER, "https://www.bilibili.com/").execute().body();
        cn.hutool.json.JSONObject cidBean = JSONUtil.toBean(cidData, cn.hutool.json.JSONObject.class);
        Object cid = cidBean.getJSONArray("data").getJSONObject(0).get("cid");
        String videoUrl = HttpRequest.get("https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + videoId + "&qn=80&type=mp4&platform=html5&high_quality=1").header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:102.0) Gecko/20100101 Firefox/102.0").header(Header.REFERER, "https://www.bilibili.com/").header(Header.COOKIE, "SESSDATA=8ede3933%2C1708355230%2C7b8c1%2A823KMOiheJYc5wT11sPue493X6zqc60iVcCvvTAZr5JB-19ayWygcVc2MN10ZR5fov054XpQAAGQA;").execute().body();
        cn.hutool.json.JSONObject videoBean = JSONUtil.toBean(videoUrl, cn.hutool.json.JSONObject.class);
        return videoBean.getJSONObject("data").getJSONArray("durl").getJSONObject(0).getStr("url");
    }
    /**
     * B站主页解析
     */
    public static JSONObject bilibiliHost(String uid, Integer page) {
//        https://api.bilibili.com/x/space/wbi/arc/search?mid=14739873&ps=30&tid=0&pn=1&keyword=&order=pubdate&platform=web&order_avoided=true
        String body = HttpRequest.get(
                        String.format("https://api.bilibili.com/x/space/wbi/arc/search?mid=%s&ps=30&tid=0&pn=%d&keyword=&order=pubdate&platform=web&order_avoided=true"
                                , uid, page+1))
                .header(Header.USER_AGENT,
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                .header(Header.COOKIE, "SESSDATA=8ede3933%2C1708355230%2C7b8c1%2A823KMOiheJYc5wT11sPue493X6zqc60iVcCvvTAZr5JB-19ayWygcVc2MN10ZR5fov054XpQAAGQA;")
                .header(Header.REFERER, "https://www.bilibili.com/").execute().body();
        JSONObject bodyObject = JSONObject.parseObject(body);
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray lists = bodyObject.getJSONObject("data").getJSONObject("list").getJSONArray("vlist");
        for (int i = 0; i < lists.size(); i++) {
            JSONObject jsonObject = lists.getJSONObject(i);
            JSONObject item = new JSONObject();
            item.put("desc", jsonObject.getString("title"));
            item.put("cover", jsonObject.getString("pic"));
            String[] split = jsonObject.getString("length").split(":");
            item.put("duration", Integer.valueOf(split[0])*60+ Integer.valueOf(split[1]));
            item.put("video_id", jsonObject.getString("bvid"));
            item.put("time", jsonObject.getString("created"));
            item.put("url", jsonObject.getString("bvid"));
            JSONObject itemWrap = new JSONObject();
            itemWrap.put("video", item);
            rows.add(itemWrap);
            if (i == 0) {
                result.put("author", jsonObject.getString("author"));
                result.put("minCursor", -1);
                result.put("maxCursor", page + 1);
                result.put("uid", uid);
            }
        }
        result.put("rowsList", rows);
        return result;
    }


    /**
     * 微博去水印
     */
    public static JSONObject weibo(String url) {
        url = BogusUtils.commGetUrl(url);
        String videoId = null;
        if (url.contains("layerid=")) {
            videoId = BogusUtils.commGetVideoId("layerid=(\\d+)", url);
        } else if (url.contains("/tv/show/")) {
//            https://weibo.com/tv/show/1034:4969476085448754?mid=4969476715187308
            videoId = BogusUtils.commGetVideoId("/tv/show/\\d+:(\\d+)", url);
        }else
        {
//            https://weibo.com/2172061270/NtjUB8YIV
            String uid = BogusUtils.commGetVideoId("(\\w+)$",url);
            String body = HttpRequest.get(String.format("https://weibo.com/ajax/statuses/show?id=%s&locale=zh-CN",uid))
                    .header(Header.USER_AGENT, "Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)")
                    .execute().body();
            JSONObject bodyJson = JSON.parseObject(body);
            JSONObject result = new JSONObject();
            result.put("author", bodyJson.getJSONObject("user").getString("screen_name"));
            result.put("uid", bodyJson.getJSONObject("user").getString("id"));
            result.put("avatar", bodyJson.getJSONObject("user").get("profile_image_url"));
            result.put("sec_uid", bodyJson.getString("mblogid"));
            result.put("duration", bodyJson.getJSONObject("page_info").getJSONObject("media_info").getString("duration"));
            result.put("time", bodyJson.getJSONObject("page_info").getJSONObject("media_info").get("video_publish_time"));
            result.put("cover", bodyJson.getJSONObject("page_info").getJSONObject("pic_info").getJSONObject("pic_small").getString("url"));
            result.put("title", bodyJson.get("text_raw"));
            result.put("analysisPlatform", "微博");
            result.put("url", bodyJson.getJSONObject("page_info").getJSONObject("media_info").getJSONArray("playback_list").getJSONObject(1).getJSONObject("play_info").getString("url"));
            result.put("music", "{}");
            return result;
        }
        if (StringUtils.isEmpty(videoId)) {
            throw new CustomerException("抓取失败");
        }
//        https://weibo.com/tv/api/component?page=/tv/show/1011:4969476085448754
        HttpResponse execute = HttpRequest.post("https://weibo.com/tv/api/component?page=/tv/show/1034:" + videoId)
                .header(Header.USER_AGENT, "Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)")
                .header(Header.REFERER, "https://weibo.com/tv/show/1034:" + videoId)
                .header("content-type", "application/x-www-form-urlencoded")
                .body("data={\"Component_Play_Playinfo\":{\"oid\":\"1034:" +
                        videoId +
                        "\"}}")
                .execute();
        String body = execute.body();
        JSONObject bodyJson = JSON.parseObject(body).getJSONObject("data").getJSONObject("Component_Play_Playinfo");
        JSONObject result = new JSONObject();
        result.put("author", bodyJson.getString("author"));
        result.put("uid", bodyJson.getJSONObject("user").getString("id"));
        result.put("avatar", bodyJson.get("avatar"));
        result.put("sec_uid", bodyJson.getJSONObject("user").getString("id"));
        result.put("duration", bodyJson.getString("duration_time"));
        result.put("time", bodyJson.get("real_date"));
        result.put("title", bodyJson.get("title"));
        result.put("cover", bodyJson.get("cover_image"));
        result.put("analysisPlatform", "微博");
        result.put("url", bodyJson.getString("stream_url"));
        result.put("music", "{}");
        return result;
    }

    /**
     * 微博主页解析
     */
    public static JSONObject weiboHost() {
        throw new CustomerException("解析失败");
    }

    /**
     * 小红书主页
     */
    public static JSONObject xiaohongshuHost(String uid) {
//        https://www.xiaohongshu.com/user/profile/56ceb93a84edcd36168fa5e0
        String body = HttpRequest.get(String.format("https://www.xiaohongshu.com/user/profile/%s", uid))
                .header(Header.USER_AGENT,BogusUtils.getUserAgent())
          .execute().body();
        body = BogusUtils.commGetVideoId("window.__INITIAL_STATE__\\b*?=\\b*?([\\s\\S]*?);?</script>", body).replaceAll("undefined", "null");
        JSONObject bodyObject = JSONObject.parseObject(body);
        JSONObject result = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONArray lists = bodyObject.getJSONObject("user").getJSONArray("notes").getJSONArray(0);
        for (int i = 0; i < lists.size(); i++) {
            JSONObject jsonObject = lists.getJSONObject(i);
            JSONObject item = new JSONObject();
            item.put("desc", jsonObject.getJSONObject("noteCard").getString("displayTitle"));
            item.put("cover", jsonObject.getJSONObject("noteCard").getJSONObject("cover").getJSONArray("infoList").getJSONObject(1).getString("url"));
            item.put("duration", jsonObject.getString("mid"));
            item.put("video_id", jsonObject.getString("id"));
//            item.put("time", jsonObject.getString("created"));
            item.put("url", jsonObject.getString("id"));
            JSONObject itemWrap = new JSONObject();
            itemWrap.put("video", item);
            rows.add(itemWrap);
            if (i == 0) {
                result.put("author", jsonObject.getJSONObject("noteCard").getJSONObject("user").getString("nickname"));
                result.put("avatar", jsonObject.getJSONObject("noteCard").getJSONObject("user").getString("avatar"));
                result.put("minCursor", -1);
                result.put("maxCursor", -1);
                result.put("uid", jsonObject.getJSONObject("noteCard").getJSONObject("user").getString("userId"));
            }
        }
        result.put("rowsList", rows);
        return result;
    }

    /**
     * 小红书链接
     */
    public static String xiaohongshuUrlById(String uid){
        String detailHtml = HttpRequest.get("https://www.xiaohongshu.com/explore/" + uid).header(Header.COOKIE, "abRequestId=4f7508af-795a-5968-9f0a-e34e084095e5; webBuild=3.4.1; xsecappid=xhs-pc-web; a1=18a0682d5deznw4i2yc3kth9atzwi56817yc0z8dq50000351385; webId=824c6c968fd0b7308e20ab50dada5090; gid=yY08KYJfqSy0yY08KYJf2WuYfduIE43JvSqTvfA9xj09uS28E32KY8888q2yqY2800DKDi2S; web_session=030037a3ed8dbb601a529c9fa6234a64d3ea3e; websectiga=3fff3a6f9f07284b62c0f2ebf91a3b10193175c06e4f71492b60e056edcdebb2; sec_poison_id=5b0f5e6d-6e87-42fc-b02c-2366f7eda33f").header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36").execute().body();
        String detailStr = BogusUtils.commGetVideoId("window\\.__INITIAL_STATE__=(\\{.*?\\}\\})\\s*</script>", detailHtml).replaceAll("undefined", "null");
        cn.hutool.json.JSONObject bean = JSONUtil.toBean(detailStr, cn.hutool.json.JSONObject.class);
        cn.hutool.json.JSONObject detailJson = bean.getJSONObject("note").getJSONObject("noteDetailMap").getJSONObject((String) bean.getJSONObject("note").get("firstNoteId"));
        return detailJson.getJSONObject("note").getJSONObject("video").getJSONObject("media").getJSONObject("stream").getJSONArray("h264").getJSONObject(0).getStr("masterUrl");
    }
    public static void main(String[] args) {
        cn.hutool.json.JSONObject weibo = OwnVideoRemoveWatermarkToolApiConstant.weibo("https://weibo.com/tv/show/1034:4969813454291018?mid=4969837262014162");
        System.out.println(weibo.toString());
    }
}
