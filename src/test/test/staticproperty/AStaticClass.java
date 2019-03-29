package staticproperty;

public class AStaticClass {

    private static String privateStaticString;

    public String publicString;
    private String privateString;

    public static String getPrivateStaticString() {
        return privateStaticString;
    }

    public static void setPrivateStaticString(String privateStaticString) {
        AStaticClass.privateStaticString = privateStaticString;
    }

    public String getPublicString() {
        return publicString;
    }

    public void setPublicString(String publicString) {
        this.publicString = publicString;
    }

    public String getPrivateString() {
        return privateString;
    }

    public void setPrivateString(String privateString) {
        this.privateString = privateString;
    }

    public class InnerClass {
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    public static class InnerStaticClass {
        private String a;
        private static String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public static String getB() {
            return b;
        }

        public static void setB(String b) {
            InnerStaticClass.b = b;
        }
    }
    //类的初始化顺序如下：
    //父类静态变量
    //父类静态块
    //子类静态变量
    //子类静态块
    //父类变量
    //父类普通块
    //父类构造函数(子类实例化时先要调用父类构造函数)
    //子类变量
    //子类普通块
    //子类构造函数
}
