import { TopNav } from "@/components/layout";

export default function LearnerLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <TopNav />
      <main>{children}</main>
    </div>
  );
}
