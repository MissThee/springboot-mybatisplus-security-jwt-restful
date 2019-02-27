package com.github.missthee.controller.example;

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
}
