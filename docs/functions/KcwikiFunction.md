### 舰娘百科功能模块

#### 【指令/群消息事件】查舰

根据名字查舰娘信息。数据来自kcwiki-api。

##### 1) 指令触发
   
*<子指令>: 舰娘百科*  
*<指令参数>: 舰娘名*

>  -> /欧根 舰娘百科 吹雪  
>  <- 吹雪 https://zh.kcwiki.cn/wiki/%E5%90%B9%E9%9B%AA  
> 吹雪\[驱逐舰\](12.7cm連装砲,61cm三連装魚雷,null,null)  
> -20级->吹雪改\[驱逐舰\]  
> -70级->吹雪改二\[驱逐舰\]  

##### 2) 事件触发

使用比指令更简洁的文本消息事件。语法：`<舰娘名>.`

>  -> 吹雪.  
>  <- 吹雪 https://zh.kcwiki.cn/wiki/%E5%90%B9%E9%9B%AA  
> 吹雪\[驱逐舰\](12.7cm連装砲,61cm三連装魚雷,null,null)  
> -20级->吹雪改\[驱逐舰\]  
> -70级->吹雪改二\[驱逐舰\]  

##### 配图

若在`data\hundun.fleet.prinzeugen\KcwikiFunction\GameData`里准备好对应id的立绘图片文件，则查询结果里会附带该图片。此处id是游戏内部的id。realse中只提供少数文件供测试，若需要完整数据，需用户自行下载。例如可从岛风go的缓存中取得。

#### 【配置】舰娘别名配置

查舰时可用别名查询。

手动编辑`config\hundun.fleet.prinzeugen\KcwikiFunction\ShipFuzzyNameConfig.json`配置别名，或使用指令配置别名。

>  -> 欠雷.  
>  <- 吹雪 https://zh.kcwiki.cn/wiki/%E5%90%B9%E9%9B%AA  
> 吹雪\[驱逐舰\](12.7cm連装砲,61cm三連装魚雷,null,null)  
> -20级->吹雪改\[驱逐舰\]  
> -70级->吹雪改二\[驱逐舰\] 

#### 【指令】添加舰娘别名

添加别名

*<子指令>: 添加舰娘别名*  
*<指令参数1>: 别名*
*<指令参数2>: kcwiki中的标准名*

>  -> /欧根 添加舰娘别名 欠雷 吹雪  
>  <- 已添加

#### 【指令】查询舰娘别名

查询现有的指定标准名的别名

*<子指令>: 查询舰娘别名*  
*<指令参数>: kcwiki中的标准名*

>  -> /欧根 查询舰娘别名 吹雪  
>  <- 欠雷-->吹雪

#### 【指令】删除舰娘别名

删除指定别名

*<子指令>: 删除舰娘别名*  
*<指令参数>: 别名*

>  -> /欧根 删除舰娘别名 欠雷  
>  <- 已删除

#### 【指令】搜任务

根据关键词模糊搜索得到任务id，稍后可进一步查任务详情。使用前需要先载入数据，详见“【指令】载入任务数据文件”

*<子指令>: 搜任务*  
*<指令参数>: 关键词*

>  -> /欧根 搜任务 出击南西诸岛防卫线  
>  <- 找到2个结果:  
>     id:823 水雷戦隊、南西防衛線に反復出撃せよ！  
>     id:849 南西諸島方面の敵艦隊を撃破せよ！   

#### 【指令】任务详情

查指定id的任务详情。当前只有任务内容的中文翻译，未来考虑改为使用新的数据来源。

*<子指令>: 任务详情*  
*<指令参数>: 任务id*

>  -> /欧根 任务详情 823  
>  <- 编成以轻巡为旗舰和四艘驱逐舰为基干的护卫舰队，反复出击南西诸岛防卫线(1-4)，努力确保同海域的制海权！  

#### 【指令】载入任务数据文件

任务相关的原始数据存放于`data\hundun.fleet.prinzeugen\KcwikiFunction\quest_old`，下载自[kcwiki项目](https://github.com/kcwikizh/kcdata/tree/gh-pages/_quest)，但是数据较旧且目前无人更新。未来考虑改为使用新的数据来源。

首次使用，或者数据文件有变动，需要执行该指令。

>  -> /欧根 载入任务数据文件
>  <- 导入351个文件