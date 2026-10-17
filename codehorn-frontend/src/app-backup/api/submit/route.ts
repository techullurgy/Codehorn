import { NextResponse } from 'next/server';

export async function POST(request: Request) {
  try {
    const { problemSlug, language, code } = await request.json();
    const gatewayUrl = process.env.GATEWAY_API_URL || 'http://localhost:6000';

    const response = await fetch(`${gatewayUrl}/api/submit`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ problemSlug, language, code }),
    });

    if (!response.ok) {
      throw new Error(`Gateway returned status: ${response.status}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error: any) {
    console.error('Error submitting code:', error);
    return NextResponse.json(
      { error: 'Failed to submit code', details: error.message },
      { status: 500 }
    );
  }
}
