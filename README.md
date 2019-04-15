# [DEPRECATED] ImagePicker
仿微信照片选择组件

## 演示

 ![image](https://github.com/LambertCoding/ImgPicker/blob/master/image/image1.jpg)
 ![image](https://github.com/LambertCoding/ImgPicker/blob/master/image/image2.jpg)
 ![image](https://github.com/LambertCoding/ImgPicker/blob/master/image/image3.jpg)

## 使用方法
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.LambertCoding:ImgPicker:1.0.2'
	}
```

1)初始化
```java
       ImagePickerConfig config = new ImagePickerConfig.Builder()
                .imageLoader(new ImageLoader() {    //  不限制图片框架，需要自己实现
                    @Override
                    public void displayImage(Context context, String path, ImageView imageView) {
                        Glide.with(context).load(path).centerCrop().into(imageView);
                    }
                })
                .showCamera(true)                   //  第一个item是否显示相机,默认true
                .limited(maxSize)                   //  最多能选的张数（单选填1）

//                 .titleBarColor(Color.GRAY)        // titlebar的颜色和文字等自定义选项
//                 .titleText("选择图片")
//                 .titleTextColor(Color.parseColor("#ffffff"))
//                 .btnResId(R.drawable.selector_back_press)
//                 .btnTextColor(Color.parseColor("#ffffff"))
//                 .backResId(R.mipmap.ic_launcher)

//                 .needCrop(true)                   //  是否裁剪（单选时才有效）,如果裁剪就不会执行压缩
//                 .cropSize(1, 1, 400, 400)         //  裁剪比例和大小
//                 .compress(false)                  //  是否压缩,默认ture
//                 .maxWidthAndHeight(720, 960)      //  压缩最大尺寸，默认720*960
//                 .quality(80)                      //  压缩质量，默认80
                .callback(new SimpleSelectListener() {
                    @Override
                    public void onSelect(List<ImageItem> data) {
                        handleImages(data);			//  接收回调，如果是单选data只有一个元素
                    }
                })
                .build();

        ImagePicker.getInstance().setConfig(config);
```
2)开启选择
```java
	ImagePicker.getInstance().open(MainActivity.this);// 最多选maxSize张
	ImagePicker.getInstance().open(MainActivity.this, 5);//  选5张
```

3)清空引用
```java
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImagePicker.getInstance().clear();
    }
```
