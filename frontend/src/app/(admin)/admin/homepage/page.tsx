"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { apiGet, apiPost, apiPut, apiDelete } from "@/lib/api/client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogFooter,
  DialogClose,
} from "@/components/ui/dialog";
import {
  Layout,
  ArrowLeft,
  Plus,
  Trash2,
  Pencil,
  ChevronDown,
  ChevronUp,
} from "lucide-react";
import Link from "next/link";

interface SectionBlock {
  id: string;
  sectionId: string;
  blockType: string;
  orderIndex: number;
  enabled: boolean;
}

interface HomepageSection {
  id: string;
  title: string;
  position: string;
  orderIndex: number;
  enabled: boolean;
  createdAt: string;
}

interface HomepageResponse {
  section: HomepageSection;
  blocks: SectionBlock[];
}

interface PaginatedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

const positionOptions = ["TOP", "LEFT", "CENTER", "RIGHT", "BOTTOM"];

export default function HomepagePage() {
  const queryClient = useQueryClient();
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editingSection, setEditingSection] = useState<HomepageSection | null>(null);
  const [expandedSections, setExpandedSections] = useState<Set<string>>(new Set());

  // Form state
  const [sectionTitle, setSectionTitle] = useState("");
  const [sectionPosition, setSectionPosition] = useState("TOP");
  const [sectionOrderIndex, setSectionOrderIndex] = useState(0);
  const [sectionEnabled, setSectionEnabled] = useState(true);

  const { data, isLoading, isError } = useQuery<PaginatedResponse<HomepageResponse>>({
    queryKey: ["admin", "homepage", "sections"],
    queryFn: () =>
      apiGet<PaginatedResponse<HomepageResponse>>(
        "/api/public/homepage?page=0&size=50"
      ),
  });

  const createSectionMutation = useMutation({
    mutationFn: (payload: {
      title: string;
      position: string;
      orderIndex: number;
      enabled: boolean;
    }) => apiPost<HomepageSection>("/api/admin/homepage/sections", payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "homepage"] });
      setDialogOpen(false);
      resetForm();
    },
  });

  const updateSectionMutation = useMutation({
    mutationFn: ({
      sectionId,
      payload,
    }: {
      sectionId: string;
      payload: { title: string; position: string; orderIndex: number; enabled: boolean };
    }) => apiPut<HomepageSection>(`/api/admin/homepage/sections/${sectionId}`, payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "homepage"] });
      setDialogOpen(false);
      setEditingSection(null);
      resetForm();
    },
  });

  const deleteSectionMutation = useMutation({
    mutationFn: (sectionId: string) =>
      apiDelete(`/api/admin/homepage/sections/${sectionId}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["admin", "homepage"] });
    },
  });

  function resetForm() {
    setSectionTitle("");
    setSectionPosition("TOP");
    setSectionOrderIndex(0);
    setSectionEnabled(true);
  }

  function openEditDialog(section: HomepageSection) {
    setEditingSection(section);
    setSectionTitle(section.title);
    setSectionPosition(section.position);
    setSectionOrderIndex(section.orderIndex);
    setSectionEnabled(section.enabled);
    setDialogOpen(true);
  }

  function handleSave() {
    const payload = {
      title: sectionTitle,
      position: sectionPosition,
      orderIndex: sectionOrderIndex,
      enabled: sectionEnabled,
    };

    if (editingSection) {
      updateSectionMutation.mutate({ sectionId: editingSection.id, payload });
    } else {
      createSectionMutation.mutate(payload);
    }
  }

  function toggleExpanded(id: string) {
    setExpandedSections((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  }

  const sections = data?.content ?? [];
  const isMutating =
    createSectionMutation.isPending || updateSectionMutation.isPending;

  const inputClass = "border-slate-700 bg-slate-800 text-slate-100";
  const labelClass = "mb-1 block text-sm text-slate-400";

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-slate-950 via-purple-950 to-slate-900 px-6 py-10">
      <div className="pointer-events-none absolute left-[8%] top-16 h-80 w-80 rounded-full bg-purple-500/20 blur-3xl" />
      <div className="pointer-events-none absolute right-[12%] top-10 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />

      <div className="relative mx-auto max-w-5xl space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between rounded-3xl border border-slate-800 bg-slate-900/60 p-8 shadow-2xl shadow-purple-500/10 backdrop-blur-xl">
          <div className="flex items-center gap-4">
            <Link href="/admin">
              <Button variant="ghost" size="icon" className="text-slate-400 hover:text-slate-200">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <div>
              <h1 className="flex items-center gap-3 bg-gradient-to-r from-purple-200 via-pink-100 to-purple-200 bg-clip-text text-3xl font-semibold tracking-tight text-transparent">
                <Layout className="h-8 w-8 text-purple-400" />
                Homepage CMS
              </h1>
              <p className="mt-1 text-slate-400">
                Manage homepage sections and content blocks.
              </p>
            </div>
          </div>

          <Dialog
            open={dialogOpen}
            onOpenChange={(open) => {
              setDialogOpen(open);
              if (!open) {
                setEditingSection(null);
                resetForm();
              }
            }}
          >
            <DialogTrigger asChild>
              <Button className="bg-gradient-to-r from-purple-600 to-pink-600 text-white hover:from-purple-700 hover:to-pink-700">
                <Plus className="mr-2 h-4 w-4" />
                New Section
              </Button>
            </DialogTrigger>
            <DialogContent className="border-slate-700 bg-slate-900 text-slate-100">
              <DialogHeader>
                <DialogTitle className="text-slate-100">
                  {editingSection ? "Edit Section" : "Create Section"}
                </DialogTitle>
              </DialogHeader>
              <div className="space-y-4 py-2">
                <div>
                  <label className={labelClass}>Title</label>
                  <Input
                    value={sectionTitle}
                    onChange={(e) => setSectionTitle(e.target.value)}
                    placeholder="Section title"
                    className={inputClass}
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className={labelClass}>Position</label>
                    <select
                      value={sectionPosition}
                      onChange={(e) => setSectionPosition(e.target.value)}
                      className="h-9 w-full rounded-md border border-slate-700 bg-slate-800 px-3 text-sm text-slate-100 outline-none focus:border-purple-500"
                    >
                      {positionOptions.map((p) => (
                        <option key={p} value={p}>
                          {p}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className={labelClass}>Order Index</label>
                    <Input
                      type="number"
                      value={sectionOrderIndex}
                      onChange={(e) => setSectionOrderIndex(Number(e.target.value))}
                      className={inputClass}
                    />
                  </div>
                </div>
                <label className="flex items-center gap-2 text-sm text-slate-300">
                  <input
                    type="checkbox"
                    checked={sectionEnabled}
                    onChange={(e) => setSectionEnabled(e.target.checked)}
                    className="rounded border-slate-600"
                  />
                  Enabled
                </label>
              </div>
              <DialogFooter>
                <DialogClose asChild>
                  <Button variant="outline" className="border-slate-700 text-slate-300">
                    Cancel
                  </Button>
                </DialogClose>
                <Button
                  onClick={handleSave}
                  disabled={!sectionTitle || isMutating}
                  className="bg-gradient-to-r from-purple-600 to-pink-600 text-white"
                >
                  {isMutating ? "Saving..." : editingSection ? "Update" : "Create"}
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>

        {/* Sections List */}
        <div className="space-y-4">
          {isLoading ? (
            <div className="flex h-64 items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/60">
              <div className="h-8 w-8 animate-spin rounded-full border-2 border-purple-500 border-t-transparent" />
            </div>
          ) : isError ? (
            <div className="flex h-64 items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/60 text-slate-400">
              Failed to load homepage sections.
            </div>
          ) : sections.length === 0 ? (
            <div className="flex h-64 items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/60 text-slate-500">
              No sections found. Create one to get started.
            </div>
          ) : (
            sections.map((item) => {
              const section = item.section;
              const blocks = item.blocks;
              const isExpanded = expandedSections.has(section.id);

              return (
                <div
                  key={section.id}
                  className="rounded-2xl border border-slate-800 bg-slate-900/60 backdrop-blur-xl"
                >
                  <div className="flex items-center justify-between p-5">
                    <div className="flex items-center gap-3">
                      <button
                        onClick={() => toggleExpanded(section.id)}
                        className="text-slate-400 hover:text-slate-200"
                      >
                        {isExpanded ? (
                          <ChevronUp className="h-5 w-5" />
                        ) : (
                          <ChevronDown className="h-5 w-5" />
                        )}
                      </button>
                      <div>
                        <h3 className="font-semibold text-slate-200">
                          {section.title}
                        </h3>
                        <div className="mt-1 flex items-center gap-2">
                          <Badge variant="outline" className="border-slate-700 text-xs text-slate-400">
                            {section.position}
                          </Badge>
                          <span className="text-xs text-slate-500">
                            Order: {section.orderIndex}
                          </span>
                          <Badge
                            variant={section.enabled ? "default" : "secondary"}
                            className="text-xs"
                          >
                            {section.enabled ? "Enabled" : "Disabled"}
                          </Badge>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Button
                        variant="ghost"
                        size="icon-sm"
                        onClick={() => openEditDialog(section)}
                        className="text-slate-400 hover:text-slate-200"
                      >
                        <Pencil className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="icon-sm"
                        onClick={() => {
                          if (confirm("Delete this section?")) {
                            deleteSectionMutation.mutate(section.id);
                          }
                        }}
                        className="text-slate-400 hover:text-red-400"
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>

                  {isExpanded && (
                    <div className="border-t border-slate-800 px-5 py-4">
                      <h4 className="mb-2 text-sm font-medium text-slate-400">
                        Blocks ({blocks.length})
                      </h4>
                      {blocks.length === 0 ? (
                        <p className="text-sm text-slate-500">
                          No blocks in this section.
                        </p>
                      ) : (
                        <div className="space-y-2">
                          {blocks.map((block) => (
                            <div
                              key={block.id}
                              className="flex items-center justify-between rounded-lg border border-slate-800 bg-slate-950/50 px-4 py-2"
                            >
                              <div className="flex items-center gap-3">
                                <span className="font-mono text-xs text-slate-500">
                                  #{block.orderIndex}
                                </span>
                                <Badge variant="outline" className="border-slate-700 text-xs text-slate-300">
                                  {block.blockType}
                                </Badge>
                              </div>
                              <Badge
                                variant={block.enabled ? "default" : "secondary"}
                                className="text-xs"
                              >
                                {block.enabled ? "Enabled" : "Disabled"}
                              </Badge>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  )}
                </div>
              );
            })
          )}
        </div>
      </div>
    </main>
  );
}
