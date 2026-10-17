import AppLayout from '../components-backup/AppLayout';
import ProblemList from '../components-backup/ProblemList';
import { fetchProblemsServer } from './api/problems-helper';

export const dynamic = 'force-dynamic';

export default async function Page() {
  const problems = await fetchProblemsServer()

  return (
    <AppLayout initialProblems={problems}>
      <ProblemList />
    </AppLayout>
  );
}
