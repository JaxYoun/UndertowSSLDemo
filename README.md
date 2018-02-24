#项目简介
1.这是一个简单的Spring-Boot项目，使用了Undertow作为容器。
2.配置了用JDK的keytool.exe本地生成的SSL证书。
3.为Undertow配置了多端口监听，分别监听8080(http)、9090(https)，并将http请求后台重定向到https接口。
4.同时开通了Undertow的http2.0功能。
