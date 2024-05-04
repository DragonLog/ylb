> **写在前面的话：整体是一个很好的基于SpringBoot+Vue的前后端分离微服务项目，使用dubbo+zookeeper进行远程方法调用，同时包含redis缓存、token鉴权、httpclient请求短信和身份证信息验证接口、集成快钱支付、定时任务等功能。可以很好的练习涉及金融投资项目业务的开发。**
## 盈利宝（前后端分离微服务）
***
> **小贴士**
1. **整个教学视频重温了一遍最基础的Vue开发流程，学完能够实现大多数前端业务逻辑。**  
2. **我没有像王老师那样封装httpclient工具类，直接就在代码里写了，主要是因为我们用的短信接口都不一样，所以干脆就自己百度自己写了。**  
3.  **那个实名认证的短信验证功能其实真没必要再写一遍了，所以我前后端都直接砍掉了。**  
4.  **因为项目是个金融项目，是要跟钱打交道的，所以业务逻辑要严谨，高并发下还要保证数据一致性，本项目在代码层面使用synchronized修饰方法来进行同步，数据库层面上通过注解式事务和对select操作加for update排它锁来保证数据一致性，这里上锁的门道其实还挺深的，既要保证数据一致性又要保证不会死锁和性能，建议专门去搜“MySQL锁机制”深入理解一下。** 
5.   **如果select...for update的where字段不是主键的话记得加唯一索引，否则就不是锁一条数据了，而是锁整张表。**  
6.   **有些枚举类的属性值我自己定义和修改过，比如成功的code是1000，我自己写的是200.**  
7. **redis、mysql和zookeeper我都是在本地虚拟机用docker部署的，我建议你也这么做，尽量让宿主机清爽一点，docker起中间件真的很好用。**  
8.  **实际上整个开发过程最难的我觉得是在跟“快钱”交互那块，如你所见这个项目是两年前的了，快钱多多少少更新了一点，就比如它的官网，原来的“电脑网站支付”已经变成网银支付了，我点进去看demo和文档都有些许改变（其实也没有改特别多），我用它最新的demo提交老是返给我“未开通hat分账”，我一脸懵逼不知道咋解决，只好老老实实回头用王老师的老快钱demo，没想到还能用！所以直接建议大家用王老师的老快钱demo，我已经放到仓库里面了。**  
9.    **快钱里有个拼接串的appendParam非常坑，写的巨烂，如果你验签总是失败而且哪里都找不出问题，我建议你直接检查一下这个方法。（这个方法在FI_ALL.jsp和receive.jsp里都有，而且写的还不一样！）。** 
10. **注意一下前后端的token验证，前端在request拦截器里加路径，后端在WebMvcConfiguration里加，到支付服务那里，我感觉王老师已经放飞自我了，一点对接口的鉴权验证措施都没有。**   
11. **localStorage在你关闭浏览器后也会一直存在，这会影响token的有效性，所以我加了一个“退出登录”按钮来清除localStorage中的相关数据，当然如果token出问题，前端会自动跳到登录界面的，这无伤大雅。**
12. **在顶部的“我要投资”下拉里点击优选类或散标类选项卡时，你会发现点击另外一个页面没有反应，我认为原因出在两个选项卡对应的goLink地址是一样的，只是携带的ptype不一样，页面默认push的地址一样就不做任何动作，所以你要使用watch监听路由里ptype的变化，具体看我写的代码。**
13.  **验证码发送倒计时那里也有bug，王老师只是把小于0后的按钮样式归位，实际上这个倒计时是一个异步的线程，会在后台一直倒计时，这意味着如果你再次发送验证码，会再起一个异步线程对codeText进行修改，就会出现莫名其妙的结果。解决此问题的办法就是在倒计时为0后直接中断这个异步的线程，具体操作看我写的代码。**  
***
**[哔哩哔哩在线课程](https://www.bilibili.com/video/BV1Bo4y1771J/?spm_id_from=333.337.search-card.all.click)**
***
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show1.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show2.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show3.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show4.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show5.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show6.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show7.png)
![示例图片](https://github.com/DragonLog/ylb/blob/main/pictureForExample/show8.png)
