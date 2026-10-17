
import { http, HttpResponse } from 'msw'
import { PROBLEMS } from './data/problems';

const MOCK_HOST = "http://localhost:5001"

export const handlers = [
    http.get(`${MOCK_HOST}/api/problems`, async ({ request }) => {
        const scenario = request.headers.get('x-mock-scenario');

        if(scenario == 'empty-state') {
            return HttpResponse.json([])
        }

        return HttpResponse.json(PROBLEMS, { status: 200 })
    }),

    http.get(`${MOCK_HOST}/api/problems/:slug`, ({ request, params }) => {
        const { slug } = params
        // const scenario = request.headers.get('x-mock-scenario');

        return HttpResponse.json(PROBLEMS.find((p) => p.slug === slug) || null, { status: 200 })
    })
]