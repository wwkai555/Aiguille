package me.spencerwang.aiguille.data;

import java.util.ArrayList;
import java.util.List;

import me.spencerwang.aiguille.dao.NewsDataHelper;
import me.spencerwang.aiguille.entity.BaseCategory;
import me.spencerwang.aiguille.entity.News;

/**
 * Created by SpencerWang on 2015/5/7.
 *
 * 类说明：这个类主要用于添加本地数据，演示project效果。
 * 如果不需要可注销调用该测试数据的地方即可。
 *
 */
public class TestData {

    private NewsDataHelper mHelper;

    private List<News> testList = new ArrayList<>();
    private String testTitle = "男子盗古墓掘出8件国家级文物获刑15年";
    private String testTitle2 = "嫌犯闹市抢夺民警枪支被刑拘 2名警员受伤";
    private String testTitle3 = "詹姆斯33分骑士强势反弹 大胜公牛总比分1-1";
    private String testContent1 = "    新华网西安4月27日电(记者毛海峰) 陕西省西安市长安区村民师某(在逃)与表哥赵某，在自家院子里清理树根时发现一处商周时期古墓葬，挖掘出8件国家级文物，倒卖时因群众举报案发。西安市中院日前以盗掘古墓葬罪，判处赵某有期徒刑15年，并处罚金10万元。\n" +
            "　　西安市长安区马王镇大原村位于第一批全国重点文物保护单位丰镐遗址内，地下有大量的西周时期古墓葬，平时禁止私人进行挖掘。2014年7月，村民师某与表哥赵某在自家院子里清理树根时，发现一处古墓葬，盗掘出铜鼎2件、铜爵杯2件、铜觚1件、铜卣1件、铜簋1件、铜尊1件，共8件文物。事后，2人联系买家。2014年8月6日，在群众举报下，公安机关将赵某抓获，并当场查获被盗的8件文物。后经陕西省文物鉴定委员会鉴定，被盗8件青铜文物中，有4件为国家二级文物，4件为国家三级文物，被盗掘墓葬年代应为商末周初。\n" +
            "　　2015年3月26日，西安市中院开庭审理此案。庭审中，赵某对犯罪事实供认不讳。西安市中院审理认为，赵某为牟取非法利益，伙同他人盗掘国家重点文物保护单位丰镐遗址内古墓葬中文物，其行为已构成盗掘古墓葬罪。4月24日西安市中院宣判，赵某犯盗掘古墓葬罪，被判处有期徒刑15年，并处罚金10万元。[责任编辑：白羽]";
    private String testContent2 ="中新网昆明4月27日电 (王艳龙) 昆明市公安局官渡分局27日通报称，该局民警在出警过程中，一名涉嫌抢夺电动车的犯罪嫌疑人试图抢夺民警公务配枪，并致一名民警及一名协警轻微伤。目前，该嫌疑人因涉嫌妨害公务罪被刑事拘留。";
    private String testContent3 ="新浪体育讯　北京时间5月7日，骑士主场以106-91轻取公牛，将总比分扳成1-1。骑士失去了主场优势。勒布朗-詹姆斯拿下了33分、8个篮板和5次助攻，凯里-欧文21分，伊曼-香波特15分7个篮板。替补出场的詹姆斯-琼斯三分球9投5中，拿下了17分。公牛全场落后，不过他们带着主场优势回家。吉米-巴特勒拿下了18分，德里克-罗斯14分、10次助攻和7个篮板，保罗-加索尔11分4个篮板，迈克-邓利维8分。";
    private News.NewsImage image1 = new News.NewsImage("http://www.leawo.cn/attachment/201402/21/1880530_1392979544TgC8.jpg","http://www.leawo.cn/attachment/201402/21/1880530_1392979544TgC8.jpg");
    private News.NewsImage image2 = new News.NewsImage("http://img2.imgtn.bdimg.com/it/u=3604365830,1357438744&fm=21&gp=0.jpg","http://pic3.nipic.com/20090527/1242397_103223089_2.jpg");
    private News.NewsImage image3 = new News.NewsImage("http://cdn.wallxd.com/5199c54a1113b23600.jpg","http://cdn.wallxd.com/5199c54a1113b23600.jpg");

    public TestData(NewsDataHelper helper){
        mHelper = helper;
    }

    public void loadTestData(){
            testList.removeAll(testList);
                for(int i = 1; i < 5;i++){
                    News news = new News(i,testTitle,testContent1,image1, BaseCategory.NewsCategory.SOCIETY);
                    News news2 = new News(i+5,testTitle2,testContent2,image2,BaseCategory.NewsCategory.AMUSEMENT);
                    News news3 = new News(i+10,testTitle3,testContent3,image3,BaseCategory.NewsCategory.TECHNOLOGY);
                    testList.add(news);
                    testList.add(news2);
                    testList.add(news3);
                }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mHelper.batchInsert(testList);
                }
            }).start();
    }


    public void deleteTestData(){
        if(mHelper != null){
            mHelper.deleteAll();
        }else{
            throw  new NullPointerException();
        }
    }

}
