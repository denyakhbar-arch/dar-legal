// DAR MultiCorp & Kantor Advokat Deny Akhbar Service Worker (Safe Network-First)
const CACHE_NAME = 'dar-multicorp-v5';

self.addEventListener('install', (event) => {
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.map((name) => {
          if (name !== CACHE_NAME) {
            return caches.delete(name);
          }
        })
      );
    })
  );
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  // Only handle GET requests
  if (event.request.method !== 'GET') return;

  // Let downloads pass through directly
  if (event.request.url.includes('/downloads/')) return;

  // Safe network-first strategy: never returns undefined or empty
  event.respondWith(
    fetch(event.request)
      .then((networkResponse) => {
        // Cache valid responses for offline use
        if (networkResponse && networkResponse.status === 200) {
          const responseToCache = networkResponse.clone();
          caches.open(CACHE_NAME).then((cache) => {
            cache.put(event.request, responseToCache).catch(() => {});
          });
        }
        return networkResponse;
      })
      .catch(() => {
        // Fallback to cache if network is unavailable
        return caches.match(event.request).then((cachedResponse) => {
          if (cachedResponse) {
            return cachedResponse;
          }
          // Fallback to main page if requesting HTML
          if (event.request.headers.get('accept')?.includes('text/html')) {
            return caches.match('/index.html').then((indexCached) => {
              return indexCached || new Response('<h1>Layanan Hukum Deny Akhbar</h1><p>Koneksi offline.</p>', {
                headers: { 'Content-Type': 'text/html; charset=utf-8' }
              });
            });
          }
          return new Response('Offline', { status: 503, statusText: 'Service Unavailable' });
        });
      })
  );
});
