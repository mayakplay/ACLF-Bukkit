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

    private static int counter = 0;

    public TestController() {
        System.out.println(++counter + "st/nd/rd (I dont give a fk) controller created");
    }

    @ChatMapping("test")
    public void something(CommandResponse response, String string, String string1) {
        System.out.println(string + " : " + string1);
    }
}
```
