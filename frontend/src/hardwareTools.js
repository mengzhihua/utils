export const hardwareTools = [
  {
    id: 'screen',
    title: '屏幕检测',
    summary: '分辨率、色域、色条、坏点、帧率'
  },
  {
    id: 'keyboard',
    title: '键盘检测',
    summary: '按键码、修饰键、连发、布局点亮'
  },
  {
    id: 'mouse',
    title: '鼠标检测',
    summary: '轨迹、按键、滚轮、双击'
  },
  {
    id: 'pointer',
    title: '触控 / 笔',
    summary: '多点触控、压感、指针类型'
  },
  {
    id: 'click',
    title: '连点测速',
    summary: '5 秒 CPS，成绩留在本机'
  },
  {
    id: 'gamepad',
    title: '手柄检测',
    summary: '按键、摇杆、可选震动'
  },
  {
    id: 'audio',
    title: '扬声器检测',
    summary: '左右声道、扫频蜂鸣'
  },
  {
    id: 'camera',
    title: '摄像头检测',
    summary: '预览、切换设备、镜像、抓拍'
  },
  {
    id: 'mic',
    title: '麦克风检测',
    summary: '电平、波形、试听、本地录音'
  }
]

export function getHardwareTool(id) {
  return hardwareTools.find((item) => item.id === id)
}
