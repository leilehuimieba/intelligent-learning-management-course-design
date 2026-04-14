#!/usr/bin/env python3
"""
Convert Markdown files to basic DOCX files using pure OOXML packaging.
No external dependencies required.
"""

from __future__ import annotations

import argparse
import datetime as dt
import os
import re
import zipfile
from pathlib import Path
from xml.sax.saxutils import escape


W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main"


def _now_iso() -> str:
    return dt.datetime.utcnow().replace(microsecond=0).isoformat() + "Z"


def _core_xml(title: str) -> str:
    now = _now_iso()
    return f"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:dcterms="http://purl.org/dc/terms/"
 xmlns:dcmitype="http://purl.org/dc/dcmitype/"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <dc:title>{escape(title)}</dc:title>
  <dc:creator>Codex</dc:creator>
  <cp:lastModifiedBy>Codex</cp:lastModifiedBy>
  <dcterms:created xsi:type="dcterms:W3CDTF">{now}</dcterms:created>
  <dcterms:modified xsi:type="dcterms:W3CDTF">{now}</dcterms:modified>
</cp:coreProperties>
"""


def _app_xml() -> str:
    return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties"
 xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
  <Application>Microsoft Office Word</Application>
</Properties>
"""


def _content_types_xml() -> str:
    return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
  <Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
  <Override PartName="/word/settings.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml"/>
  <Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>
  <Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>
</Types>
"""


def _rels_xml() -> str:
    return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
  <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
  <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
</Relationships>
"""


def _document_rels_xml() -> str:
    return """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
  <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
</Relationships>
"""


def _settings_xml() -> str:
    return f"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:settings xmlns:w="{W_NS}">
  <w:zoom w:percent="100"/>
  <w:defaultTabStop w:val="720"/>
  <w:characterSpacingControl w:val="doNotCompress"/>
</w:settings>
"""


def _styles_xml() -> str:
    return f"""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:styles xmlns:w="{W_NS}">
  <w:docDefaults>
    <w:rPrDefault>
      <w:rPr>
        <w:rFonts w:ascii="Calibri" w:hAnsi="Calibri" w:eastAsia="Microsoft YaHei" w:cs="Calibri"/>
        <w:sz w:val="22"/>
        <w:szCs w:val="22"/>
      </w:rPr>
    </w:rPrDefault>
    <w:pPrDefault>
      <w:pPr>
        <w:spacing w:line="360" w:lineRule="auto"/>
      </w:pPr>
    </w:pPrDefault>
  </w:docDefaults>

  <w:style w:type="paragraph" w:default="1" w:styleId="Normal">
    <w:name w:val="Normal"/>
    <w:qFormat/>
  </w:style>

  <w:style w:type="paragraph" w:styleId="Heading1">
    <w:name w:val="heading 1"/>
    <w:basedOn w:val="Normal"/>
    <w:next w:val="Normal"/>
    <w:qFormat/>
    <w:pPr><w:spacing w:before="240" w:after="120"/><w:outlineLvl w:val="0"/></w:pPr>
    <w:rPr><w:b/><w:sz w:val="34"/><w:szCs w:val="34"/></w:rPr>
  </w:style>

  <w:style w:type="paragraph" w:styleId="Heading2">
    <w:name w:val="heading 2"/>
    <w:basedOn w:val="Normal"/>
    <w:next w:val="Normal"/>
    <w:qFormat/>
    <w:pPr><w:spacing w:before="180" w:after="80"/><w:outlineLvl w:val="1"/></w:pPr>
    <w:rPr><w:b/><w:sz w:val="30"/><w:szCs w:val="30"/></w:rPr>
  </w:style>

  <w:style w:type="paragraph" w:styleId="Heading3">
    <w:name w:val="heading 3"/>
    <w:basedOn w:val="Normal"/>
    <w:next w:val="Normal"/>
    <w:qFormat/>
    <w:pPr><w:spacing w:before="120" w:after="60"/><w:outlineLvl w:val="2"/></w:pPr>
    <w:rPr><w:b/><w:sz w:val="26"/><w:szCs w:val="26"/></w:rPr>
  </w:style>

  <w:style w:type="paragraph" w:styleId="Code">
    <w:name w:val="Code"/>
    <w:basedOn w:val="Normal"/>
    <w:pPr><w:spacing w:before="40" w:after="40"/></w:pPr>
    <w:rPr>
      <w:rFonts w:ascii="Consolas" w:hAnsi="Consolas" w:eastAsia="Consolas" w:cs="Consolas"/>
      <w:sz w:val="20"/><w:szCs w:val="20"/>
    </w:rPr>
  </w:style>
</w:styles>
"""


def _para(text: str, style: str | None = None) -> str:
    text_escaped = escape(text)
    ppr = f"<w:pPr><w:pStyle w:val=\"{style}\"/></w:pPr>" if style else ""
    return f"<w:p>{ppr}<w:r><w:t xml:space=\"preserve\">{text_escaped}</w:t></w:r></w:p>"


def _parse_markdown(md_text: str) -> tuple[str, str]:
    lines = md_text.splitlines()
    body: list[str] = []
    in_code = False
    title = "Document"
    got_title = False

    for raw in lines:
        line = raw.rstrip("\n")

        if line.strip().startswith("```"):
            in_code = not in_code
            continue

        if in_code:
            body.append(_para(line, "Code"))
            continue

        if not line.strip():
            body.append("<w:p/>")
            continue

        h1 = re.match(r"^#\s+(.*)$", line)
        if h1:
            text = h1.group(1).strip()
            if not got_title:
                title = text
                got_title = True
            body.append(_para(text, "Heading1"))
            continue

        h2 = re.match(r"^##\s+(.*)$", line)
        if h2:
            body.append(_para(h2.group(1).strip(), "Heading2"))
            continue

        h3 = re.match(r"^###\s+(.*)$", line)
        if h3:
            body.append(_para(h3.group(1).strip(), "Heading3"))
            continue

        bullet = re.match(r"^\s*[-*]\s+(.*)$", line)
        if bullet:
            body.append(_para(f"• {bullet.group(1)}"))
            continue

        numbered = re.match(r"^\s*(\d+\.\s+.*)$", line)
        if numbered:
            body.append(_para(numbered.group(1)))
            continue

        # Table row to plain text line.
        if line.strip().startswith("|") and line.strip().endswith("|"):
            if re.search(r"\|\s*-{3,}\s*\|", line):
                continue
            cells = [c.strip() for c in line.strip().strip("|").split("|")]
            body.append(_para("  |  ".join(cells)))
            continue

        body.append(_para(line))

    sect = (
        "<w:sectPr>"
        "<w:pgSz w:w=\"11906\" w:h=\"16838\"/>"
        "<w:pgMar w:top=\"1440\" w:right=\"1440\" w:bottom=\"1440\" w:left=\"1440\" "
        "w:header=\"708\" w:footer=\"708\" w:gutter=\"0\"/>"
        "</w:sectPr>"
    )

    xml = (
        f"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        f"<w:document xmlns:w=\"{W_NS}\" "
        "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
        f"<w:body>{''.join(body)}{sect}</w:body>"
        "</w:document>"
    )
    return xml, title


def md_to_docx(md_path: Path, docx_path: Path) -> None:
    md_text = md_path.read_text(encoding="utf-8")
    document_xml, title = _parse_markdown(md_text)

    docx_path.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(docx_path, "w", zipfile.ZIP_DEFLATED) as zf:
        zf.writestr("[Content_Types].xml", _content_types_xml())
        zf.writestr("_rels/.rels", _rels_xml())
        zf.writestr("docProps/core.xml", _core_xml(title))
        zf.writestr("docProps/app.xml", _app_xml())
        zf.writestr("word/document.xml", document_xml)
        zf.writestr("word/styles.xml", _styles_xml())
        zf.writestr("word/settings.xml", _settings_xml())
        zf.writestr("word/_rels/document.xml.rels", _document_rels_xml())


def main() -> int:
    parser = argparse.ArgumentParser(description="Convert Markdown files to DOCX.")
    parser.add_argument("inputs", nargs="+", help="Markdown files")
    args = parser.parse_args()

    for in_path in args.inputs:
        md_path = Path(in_path)
        if not md_path.exists() or md_path.suffix.lower() != ".md":
            raise FileNotFoundError(f"Invalid markdown file: {md_path}")
        out_path = md_path.with_suffix(".docx")
        md_to_docx(md_path, out_path)
        print(f"Generated: {out_path}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())

