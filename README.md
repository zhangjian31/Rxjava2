# Rxjava2
1、just传递的只是泛型T的列表，依次发送数据。如果传递一个list，那么发送的实际上是一个T,list是作为整个对象发送的，
2、from发送的是数组或列表展开，发送list的一个item。
3、range发射特定整数序列Observable
4、timer相当于postDelay
5、interval按照固定时间间隔发射整数序列
6、repeat重复发送数据
7、Action和Func函数区别
   Action包装的是有参数的
   Func包装的是有返回值的
8、map对象转换，flatmap对象转换+Observable封装
9、just参数泛型整体，from参数数组、列表，当from=1是调用just