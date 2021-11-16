### MovieBar-watch video on pc on mobile phone

> This is a project that helps people watch videos on computer hard drives on smartphones. It is no longer necessary to save the entire movie on the mobile phone, as long as it is saved to the hard disk of the computer, it can be played on the mobile phone.

> MovieBar是一个可以在手机端观看电脑上已下载视频的开源项目。只需要在电脑端启动本项目，然后在手机浏览器中打开对应网页，即可通过局域网观看电脑上的影片。


### 使用步骤-step

1. 下载安装JDK11+
2. 下载安装maven
3. clone本项目到本地，在项目根目录，使用maven命令`mvn clean package -DskipTests`打包成jar包。
4. 使用`java -jar movie-bar-0.0.1-SNAPSHOT.jar`即可运行本项目。
5. 在PC端浏览器访问：http://localhost:8888/，点击“添加文件夹”添加电脑端存放影片的文件夹。等待一会，程序会为影片生成缩略图和GIF供手机端浏览简介。
6. 手机端访问：http://your pc local ip:8888/即可访问。

### 使用限制-defect

1. 目前只支持mp4格式影片，后续会逐渐添加其他格式。
2. 当前仍需要本机安装JDK和Maven来运行项目，后续会封装成EXE。
3. 本项目当前只包含了springboot开发的server端代码。前端代码（使用Element-UI开）暂未上传。

### about us

[coderbbb](https://www.coderbbb.com)