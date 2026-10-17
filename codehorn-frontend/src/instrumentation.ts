
export async function register() {
  console.log('[Instrumentation] Runtime:', process.env.NEXT_RUNTIME);
  console.log('[Instrumentation] ENABLE_MSW:', process.env.ENABLE_MSW);

  if (
    process.env.NEXT_RUNTIME === 'nodejs' &&
    process.env.ENABLE_MSW === 'true'
  ) {
    const { server } = await import('./mocks/server');
    server.listen({ onUnhandledFrame: 'bypass' });
  }
}