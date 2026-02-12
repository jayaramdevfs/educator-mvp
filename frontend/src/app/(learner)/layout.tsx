export default function LearnerLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="min-h-screen bg-[var(--surface)]">
      <header className="border-b bg-white/85 px-6 py-4 backdrop-blur">
        <p className="font-mono text-xs uppercase tracking-[0.22em] text-zinc-500">Learner Workspace</p>
      </header>
      {children}
    </div>
  );
}

