const packs = {
  zh: {
    brand: 'Utils 本机概览',
    subtitle: 'Chrome 新标签页 · 配置与占用',
    search: '搜索 Google，或输入网址后回车',
    cpu: 'CPU',
    memory: '内存',
    battery: '电池',
    system: '系统',
    cores: '逻辑核',
    charging: '充电',
    processor: '处理器',
    storage: '存储 / 显示器 / GPU',
    sampling: '正在采样 CPU…',
    noDisks: '未读到磁盘列表',
    viewport: '当前视口',
    builtin: '内置',
    noteExt: 'CPU / 内存 / 磁盘 / 显示器来自 chrome.system（整机）。电池、网络、GPU 来自页面接口。新标签页可直接搜索。',
    noteWeb: '当前不是扩展环境，已降级为网页接口（看不到整机任务管理器）。请在 chrome://extensions 加载本目录。',
    popupTitle: '本机占用',
    popupHint: '点工具栏图标也可打开新标签页仪表盘'
  },
  en: {
    brand: 'Utils machine overview',
    subtitle: 'Chrome new tab · config and usage',
    search: 'Search Google, or type a URL and press Enter',
    cpu: 'CPU',
    memory: 'Memory',
    battery: 'Battery',
    system: 'System',
    cores: 'cores',
    charging: 'chg',
    processor: 'Processor',
    storage: 'Storage / display / GPU',
    sampling: 'Sampling CPU…',
    noDisks: 'No storage units',
    viewport: 'Viewport',
    builtin: 'built-in',
    noteExt: 'CPU / RAM / disks / displays come from chrome.system. Battery, network and GPU use page APIs.',
    noteWeb: 'Not running as an extension. Load this folder in chrome://extensions to see full-machine stats.',
    popupTitle: 'Machine usage',
    popupHint: 'Toolbar icon opens the compact view; new tabs show the full dashboard'
  },
  ja: {
    brand: 'Utils マシン概要',
    subtitle: 'Chrome 新しいタブ · 構成と使用量',
    search: 'Google 検索、または URL を入力して Enter',
    cpu: 'CPU',
    memory: 'メモリ',
    battery: 'バッテリー',
    system: 'システム',
    cores: '論理コア',
    charging: '充電中',
    processor: 'プロセッサ',
    storage: 'ストレージ / ディスプレイ / GPU',
    sampling: 'CPU を計測中…',
    noDisks: 'ディスク一覧なし',
    viewport: 'ビューポート',
    builtin: '内蔵',
    noteExt: 'CPU / メモリ / ディスク / ディスプレイは chrome.system（マシン全体）。電池・ネットワーク・GPU はページ API です。',
    noteWeb: '拡張機能として読み込まれていません。chrome://extensions からこのフォルダを読み込んでください。',
    popupTitle: 'マシン使用量',
    popupHint: 'ツールバーからも新しいタブの概要を開けます'
  },
  de: {
    brand: 'Utils Geräteüberblick',
    subtitle: 'Chrome-Neuer-Tab · Konfiguration und Auslastung',
    search: 'Google suchen oder URL eingeben und Enter',
    cpu: 'CPU',
    memory: 'Speicher',
    battery: 'Akku',
    system: 'System',
    cores: 'Kerne',
    charging: 'lädt',
    processor: 'Prozessor',
    storage: 'Speicher / Display / GPU',
    sampling: 'CPU wird gemessen…',
    noDisks: 'Keine Datenträger',
    viewport: 'Viewport',
    builtin: 'intern',
    noteExt: 'CPU / RAM / Datenträger / Displays kommen von chrome.system. Akku, Netz und GPU nutzen Seiten-APIs.',
    noteWeb: 'Kein Erweiterungskontext. Laden Sie diesen Ordner unter chrome://extensions, um die ganze Maschine zu sehen.',
    popupTitle: 'Geräteauslastung',
    popupHint: 'Das Symbol in der Symbolleiste öffnet die Kurzansicht'
  }
}

export function localePack() {
  const lang = String(navigator.language || 'en').toLowerCase()
  if (lang.startsWith('zh')) return packs.zh
  if (lang.startsWith('ja')) return packs.ja
  if (lang.startsWith('de')) return packs.de
  return packs.en
}
