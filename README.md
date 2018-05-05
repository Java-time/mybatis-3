MyBatis 最新中文注释版
======================

[![Build Status](https://travis-ci.org/mybatis/mybatis-3.svg?branch=master)](https://travis-ci.org/mybatis/mybatis-3)
[![Coverage Status](https://coveralls.io/repos/mybatis/mybatis-3/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/mybatis-3?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56199c04a193340f320005d3/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56199c04a193340f320005d3)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Stack Overflow](http://img.shields.io/:stack%20overflow-mybatis-brightgreen.svg)](http://stackoverflow.com/questions/tagged/mybatis)
[![Project Stats](https://www.openhub.net/p/mybatis/widgets/project_thin_badge.gif)](https://www.openhub.net/p/mybatis)

![mybatis](http://mybatis.github.io/images/mybatis-logo.png)

为什么有这个仓库
-------------

MyBatis 是一个优秀的 Java ORM 框架，无论是从使用方法上还是从代码结构上都是很值得学习的一个框架，但是无奈 MyBatis 的作者（以及贡献者）都极少在代码中写注释。虽然 MyBatis 的代码结构很优雅，代码也几乎自文档化，但是作为学习材料的话，如果有一些提示性的注释，将会加快我们的学习进程。起初我在 GitHub 上也有看到有一些注释版的 MyBatis Fork，但里面的注释几乎都是一句话：类名的中文翻译。因此便有了这个 Fork 仓库。

这个仓库有什么特点
---------------

- 更新快：本仓库上游仓库就是 [MyBatis 的官方仓库](https://github.com/mybatis/mybatis-3)，本人也 Watch 了 MyBatis 的官方仓库，如果其有更新的话，出于学习目的我也会第一时间去查看其更新内容，并添加注释合并到本仓库中；

- 注释全：本仓库代码目的是全面了解 MyBatis 的使用、设计、代码架构和 ORM 的原理，注释内容几乎涵盖了 MyBatis 的每一个类文件，且各个类文件的注释并不仅仅是针对当前一个类的注释，往往会联系到该类在 MyBatis 中与其他类的联系、相互作用、以及该类中使用的设计模式、Java 原理等，将所有类的关系及 Java 的知识点形成一个有联系的记忆网，增强学习效果。

如何使用这个仓库
-------------

要全面了解一个开源框架的方方面面，最好的入口就是其单元测试。由于单元测试要尽可能覆盖到所有的代码路径，因此可以达到学习的全面性；而每一个单元测试都只测试其中的某一个功能，这又可以讲学习内容分模块化进行。如果有所不清楚，还能够进行调试运行，逐步跟踪代码的运行。

因此，使用本仓库时，建议从该仓库的单元测试入手，调试时逐步进入到 MyBatis 的各个类中，查看其注释内容，最终将 MyBatis 整体串联起来。
