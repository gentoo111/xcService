maven打包中出现的一些错误:
1.repackage failed: Unable to find main class
在目录下新建一个类,随便定义一个main函数即可完成,但一个jar包打完有30几M,我也是醉了
2.Plugin not found in any plugin repository
不需要在pom中重新定义spring‐boot‐maven‐plugin插件
3.The packaging for this project did not assign a file to the build artifact
使用插件下的install:install会出这个问题,网上的解决办法是使用声明周期下的install,结果就会报错误1,因为他会视图打包成一个springboot的启动jar包,但此时我们要做的只是把jar包安装到maven仓库
4.学成在线的jar依赖比较混乱,出现好多工程互相依赖的情况,导致打包很难完成
5.xc-framework-model使用maven插件无法完成打包,需在目录下运行mvn install
6.很多工程使用maven helper插件无法进行打包和安装,需要使用声明周期中的打包才可以,如果这个也不行就要使用命令行
7.有的时候第一次打包失败,再运行一次就成功了,正常来说package会先编译,但这个插件估计有问题,第一次打包有时候不编译,导致打包成功,第二次再打包就可以了
8.测试包中的在第一次打包时不会被编译,导致依赖的类找不到,两种解决办法,第一就是注释掉,第二生命周期package两次即可解决
9.打包顺序
    a)xc-framework-parent
    b)xc-framework-utils
    c)xc-framework-common
    d)xc-framework-model
    e)xc-govern-center可以直接用install 或者package
    f)xc-service-api需要在命令行下用 mvn install
    g)xc-govern-gateway  mvn package
    h)xc-service-base-filesystem mvn package
    i)xc-service-base-id mvn package
    j)xc-service-learning 生命周期package
    k)xc-service-manage-cms 打包时报错test方法中依赖,把test方法注释使用 mvn package
    l)xc-service-manage-cms-client mvn package报找不到符号,使用生命周期打包
    m)xc-service-manage-course mvn package和生命周期均成功
    n)xc-service-manage-media 生命周期两次成功
    o)xc-service-manage-media-processor
        第一次:程序包com.xuecheng.framework.domain.media不存在
        第三次生命周期package成功
    p)xc-service-manage-order 生命周期package
    q)xc-service-search 生命周期两次
    r)xc-service-ucenter 生命周期两次
    s)xc-service-ucenter-auth  生命周期两次,测试包中的不会被编译