// FCM 백그라운드 수신용 서비스워커
// 반드시 사이트 루트(/firebase-messaging-sw.js)에서 서빙되어야 함 → static 폴더 최상위에 위치
// [문서] 백그라운드 수신: https://firebase.google.com/docs/cloud-messaging/js/receive

importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging-compat.js');

// templates/fcm/receive.html 의 firebaseConfig 와 동일한 값으로 채울 것
firebase.initializeApp({
    apiKey:            "-",
    authDomain:        "-.firebaseapp.com",
    projectId:         "-",
    storageBucket:     "-.firebasestorage.app",
    messagingSenderId: "-",
    appId:             "-:-"
});

const messaging = firebase.messaging();

// 백그라운드(탭 비활성/닫힘) 메시지 수신 → 알림 표시
messaging.onBackgroundMessage((payload) => {
    console.log('[sw] 백그라운드 수신:', payload);
    const { title, body } = payload.notification || {};
    self.registration.showNotification(title || '알림', { body: body || '' });
});
