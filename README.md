## TODOs:
| Plan  | Status  |
| ------------ | ------------ |
|Registration of Spring Addon Configurations|`complete`|
|Command sender scoped beans|`complete`|
|Translation|`complete`|
|Translation injection|`in progress`|
|Global ACLF configuration|`planning`|
|Configurable addons configurations|`planning`|
|Client resources (packets) management|`planning`|
|Bungee server linking|`planning`|

## Controllers usage

↓ This thing creates a new command `/test test` with two String args

```java
@CommandController
@CommandMapping("test")
public class TestController {

    @ChatMapping("test")
    public void test(CommandResponse response, String string, String string1) {
        System.out.println(string + " : " + string1);
    }
}
```

```java
@CommandController
@CommandMapping("test")
public class TestController {

    @ChatMapping("test")
    public void test(CommandResponse response, String string, String string1) {
        System.out.println(string + " : " + string1);
    }
}
```

#### You can set command access levels and change standard messages.

```java
@CommandController
@CommandMapping
public class TestController {

    @Documented(
        opsOnlyMessage = "You cant use this command, idiot",
	noPermissionsMessage = "NOT FOR YOU! AHAHAHA")
    @OpsOnly
    @Permitted("permission.name")
    @ChannelMapping("channel")
    public void opsOnlyCommand() {
        System.out.println("OP player is here");
    }

}
```
