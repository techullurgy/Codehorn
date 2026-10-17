import AppLayout from '../../../components-backup/AppLayout';
import ProblemDetailWrapper from '../../../components-backup/ProblemDetailWrapper';
import { notFound } from 'next/navigation';
import { fetchProblemBySlugServer } from '../../api/problems-helper';

export const dynamic = 'force-dynamic';

export default async function ProblemPage({
  params,
}: {
  params: Promise<{ slug: string }>;
}) {
  const { slug } = await params;
  const problem = await fetchProblemBySlugServer(slug)

  if (!problem) {
    notFound();
  }

  return (
    <AppLayout>
      <ProblemDetailWrapper problem={problem} />
    </AppLayout>
  );
}
