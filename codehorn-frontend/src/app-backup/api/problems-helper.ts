import { headers } from 'next/headers';
import { Problem } from '../../types';

export async function fetchProblemsServer(): Promise<Problem[]> {
  const gatewayUrl = 'http://localhost:5001';

  const incomingHeaders = await processIncomingHeaders()

  console.log("Incoming", incomingHeaders)

  const response = await fetch(`${gatewayUrl}/api/problems`, {
    cache: 'no-store', // ensures fresh data
    headers: {
      'Content-Type': 'application/json',
      ...incomingHeaders
    },
  });
  if (!response.ok) {
    throw new Error(`Gateway returned status: ${response.status}`);
  }
  return await response.json();
}

export async function fetchProblemBySlugServer(slug: string): Promise<Problem | null> {
  const gatewayUrl =  'http://localhost:5001';

  const incomingHeaders = await processIncomingHeaders()

  const response = await fetch(`${gatewayUrl}/api/problems/${slug}`, {
    cache: 'no-store',
    headers: {
      'Content-Type': 'application/json',
      ...incomingHeaders
    },
  });
  if (!response.ok) {
    throw new Error(`Gateway returned status: ${response.status}`);
  }
  return await response.json();
}

async function processIncomingHeaders(): Promise<HeadersInit> {
  const incomingHeaders = await headers()
  const mockScenario = incomingHeaders.get('x-mock-scenario')

  if(mockScenario) {
    return { 'x-mock-scenario': mockScenario }
  } else {
    return {}
  }
}