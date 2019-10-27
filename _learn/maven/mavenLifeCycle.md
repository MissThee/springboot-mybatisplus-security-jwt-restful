### maven生命周期

1. Clean 生命周期
    ```
    pre-clean
    clean
    post-clean
    ```
2. Default (or Build) 生命周期
    ```
    validate	检查工程配置是否正确，完成构建过程的所有必要信息是否能够获取到。
    initialize	初始化构建状态，例如设置属性。
    generate-sources	生成编译阶段需要包含的任何源码文件。
    process-sources	处理源代码，例如，过滤任何值（filter any value）。
    generate-resources	生成工程包中需要包含的资源文件。
    process-resources	拷贝和处理资源文件到目的目录中，为打包阶段做准备。
    compile	编译工程源码。
    process-classes	处理编译生成的文件，例如 Java Class 字节码的加强和优化。
    generate-test-sources	生成编译阶段需要包含的任何测试源代码。
    process-test-sources	处理测试源代码，例如，过滤任何值（filter any values)。
    test-compile	编译测试源代码到测试目的目录。
    process-test-classes	处理测试代码文件编译后生成的文件。
    test	使用适当的单元测试框架（例如JUnit）运行测试。
    prepare-package	在真正打包之前，为准备打包执行任何必要的操作。
    package	获取编译后的代码，并按照可发布的格式进行打包，例如 JAR、WAR 或者 EAR 文件。
    pre-integration-test	在集成测试执行之前，执行所需的操作。例如，设置所需的环境变量。
    integration-test	处理和部署必须的工程包到集成测试能够运行的环境中。
    post-integration-test	在集成测试被执行后执行必要的操作。例如，清理环境。
    verify	运行检查操作来验证工程包是有效的，并满足质量要求。
    install	安装工程包到本地仓库中，该仓库可以作为本地其他工程的依赖。
    deploy	拷贝最终的工程包到远程仓库中，以共享给其他开发人员和工程。
    ```