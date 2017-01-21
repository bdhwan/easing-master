# Real time easing animation helper <br>

This is easing animation helper class for real time animation.<br>
Android sensor's value (such as mic, accelerometers or gyroscope) is too dynamic to show real time animation. <br>
Values change too quickly so we need make it smoothly. <br>


# accelerometers animation
<img src="https://raw.githubusercontent.com/bdhwan/easing-master/master/art/acc_easing.gif" width="500px">


# how to
add you app dependency

```java
compile 'com.altamirasoft.easingmaster:easing-master:1.0.15'
```

# add helper listener
```java

        EasingHelper helper = new EasingHelper().setEasing(0.1f);
        helper.addUpdateListener(new EasingUpdateListener() {

            @Override
            public void onUpdateCurrentValue(float value) {
                //make ui update
                object.setTranslationX(value);
            }

            @Override
            public void onFinishUpdateValue(float value) {

            }
        });
        helper.start();

```

# set target value
```java

  //set target value
  helper.setTargetValue(300);

```
