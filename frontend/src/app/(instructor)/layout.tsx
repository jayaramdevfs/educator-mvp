export default function InstructorLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="min-h-screen bg-[#f7f4ee]">
      <header className="border-b bg-white px-6 py-4">
        <p className="font-mono text-xs uppercase tracking-[0.22em] text-zinc-600">Instructor Space</p>
      </header>
      {children}
    </div>
  );
}

