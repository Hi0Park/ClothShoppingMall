import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

// 1. 커스텀 메트릭: 에러 발생 비율 측정
const errorRate = new Rate('errors');

export const options = {
    // 부하 패턴 설정
    stages: [
        { duration: '30s', target: 100 },  // 30초 동안 20명까지 서서히 증가
        { duration: '1m',  target: 150 },  // 1분 동안 20명 유지 (안정성 확인)
        { duration: '30s', target: 400 },  // 30초 동안 50명까지 추가 증가 (피크 테스트)
        { duration: '1m',  target: 250 },  // 1분 동안 50명 유지
        { duration: '30s', target: 0 },   // 30초 동안 서서히 종료
    ],

    // 통과 기준 (SLO)
    thresholds: {
        'http_req_duration': ['p(95)<500'], // 전체 요청의 95%가 0.5초 이내여야 함
        'http_req_failed':   ['rate<0.01'], // HTTP 요청 실패율 1% 미만이어야 함
    },
};

// 테스트할 기본 주소
const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
    // 실제 API 호출
    const res = http.get(`${BASE_URL}/api/customer/each`);

    // 검증 (200 OK인지 확인)
    const result = check(res, {
        'is status 200': (r) => r.status === 200,
        'body size > 0': (r) => r.body.length > 0,
    });

    // 검증 실패 시 에러 레이트에 기록
    if (!result) {
        errorRate.add(1);
    }

    // 실제 사용자의 행동처럼 보이기 위한 휴식 시간 (1~2초 사이 랜덤)
    sleep(Math.random() * 1 + 1);
}

// 종료 후 리포트 출력
export function handleSummary(data) {
    return {
        'stdout': textSummary(data, { indent: ' ', enableColors: true }),
    };
}