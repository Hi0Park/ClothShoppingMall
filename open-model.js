// open-model.js
import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        constant_request_rate: {
            executor: 'constant-arrival-rate',
            rate: 500,                  // 초당 500 요청 고정
            timeUnit: '1s',
            duration: '5m',
            preAllocatedVUs: 200,        // VU 200개 미리 할당
            maxVUs: 500,                 // 필요시 500까지 늘림
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<200', 'p(99)<500'],
        http_req_failed:   ['rate<0.001'],
    },
};

export default function () {
    const res = http.get('http://host.docker.internal:8080/api/customer/each');
    check(res, { 'status 200': (r) => r.status === 200 });
    // Open Model에서는 sleep 없이도 OK - 도착률이 이미 제어됨
}