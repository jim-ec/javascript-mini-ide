package com.example.jimec.javascriptshell.js_editor_view;

import android.content.Context;

import com.example.jimec.javascriptshell.R;
import com.example.jimec.javascriptshell.Util;
import com.example.jimec.javascriptshell.code_lines.Line;
import com.example.jimec.javascriptshell.code_lines.LineList;

import static com.example.jimec.javascriptshell.Util.strbackcmp;
import static com.example.jimec.javascriptshell.Util.strcmp;

public class HtmlGenerator {

    private static final int NEUTRAL = 0;
    private static final int STRING = 1;
    private static final int COMMENT_SINGE_LINE = 2;
    private static final int COMMENT_MULTI_LINE = 3;
    private static final int KEYWORD = 4;
    private static final int NUMBER = 5;
    private static final int HEX_NUMBER = 6;
    private static final int HEX_NUMBER_PREFIX = 7;
    private static final int DECIMAL_PART_NUMBER = 8;
    private static final String[] KEYWORD_LIST = new String[]{
            "print",
            "NaN", "Infinity",

            "arguments", "await",
            "break", "case", "catch",
            "class", "const", "continue",
            "debugger", "default", "delete", "do",
            "else", "enum", "eval",
            "export", "extends", "false", "",
            "finally", "for", "function",
            "if", "implements", "import",
            "in", "instanceof", "interface",
            "let", "new",
            "null", "package", "private", "protected",
            "public", "return", "static",
            "super", "switch", "this",
            "throw", "true",
            "try", "typeof", "var", "void",
            "while", "with", "yield"
    };
    private static final char[] OPERATOR_LIST = new char[]{
            '+', '*', '/', '{', '}', '(', ')', ',', '|', '!', '?', '%', '^',
            '.', ';', '~', '=', '[', ']', '-', '&', '<', '>', '\\'
    };

    private final Context mContext;

    public HtmlGenerator(Context context) {
        mContext = context;
    }

    public String generateHtml(LineList lines) {
        StringBuilder sb = new StringBuilder();
        int currentTextType = NEUTRAL;
        int cursorLine = lines.getCursorLine();
        int cursorCol = lines.getCursorCol();

        sb.append("<!doctype html><html><head><meta charset='utf-8'/><style>")
                .append(Util.readTextFile(mContext, R.raw.html_style))
                .append("</style>")
                .append("</head><body>")
                .append("<table class='code-table'>");

        for (int lineNumber = 0; lineNumber < lines.getLines().size(); lineNumber++) {

            Line line = lines.getLines().get(lineNumber);
            String codeLine = line.getCode();

            // Create line preface and line number
            if (cursorLine == lineNumber) {
                // This is the active line
                sb.append("    <tr class='active-line'>");
            } else {
                sb.append("    <tr>");
            }
            sb.append("<td class='line-number'>").append(lineNumber + 1).append("</td>")
                    .append("<td class='code-line'>&#xFEFF;");

            // Create indent:
            for (int indent = 0; indent < line.getIndent(); indent++) {
                sb.append("    ");
            }

            // Highlight line:
            for (int i = 0; i < codeLine.length(); ) {

                char c = codeLine.charAt(i);
                boolean spanEndsAfterwards = false;
                boolean reprocessCurrentCharacter = false;

                // Cursor
                if (cursorLine == lineNumber && cursorCol == i) {
                    sb.append("<span class='cursor-container'><span id='cursor'></span></span>");
                }

                // Single line comment start
                if (strcmp(codeLine, "//", i) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-comment'>");
                    currentTextType = COMMENT_SINGE_LINE;
                }

                // Single line comment end
                else if (currentTextType == COMMENT_SINGE_LINE && (i == codeLine.length() - 1)) {
                    spanEndsAfterwards = true;
                    currentTextType = NEUTRAL;
                }

                // Multi line comment start
                else if (strcmp(codeLine, "/*", i) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-comment'>");
                    currentTextType = COMMENT_MULTI_LINE;
                }

                // Multi line comment continues
                else if (currentTextType == COMMENT_MULTI_LINE && (i == 0)) {
                    sb.append("<span class='code-highlight-comment'>");
                }

                // Multi line comment end
                else if (strbackcmp(codeLine, "*/", i) && currentTextType == COMMENT_MULTI_LINE) {
                    spanEndsAfterwards = true;
                    currentTextType = NEUTRAL;
                }

                // Multi line comment break
                else if (currentTextType == COMMENT_MULTI_LINE && (i == codeLine.length() - 1)) {
                    spanEndsAfterwards = true;
                }

                // String starting
                else if (strcmp(codeLine, "'", i) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-string'>");
                    currentTextType = STRING;
                }

                // String ending
                else if (strbackcmp(codeLine, "'", i) && currentTextType == STRING) {
                    spanEndsAfterwards = true;
                    currentTextType = NEUTRAL;
                }

                // Keyword start
                else if (Character.isLetter(c) && currentTextType == NEUTRAL && isKeyword(codeLine, i)) {
                    currentTextType = KEYWORD;
                    sb.append("<span class='code-highlight-keyword'>");
                }

                // Keyword end
                else if (!Character.isLetter(c) && currentTextType == KEYWORD) {
                    sb.append("</span>");
                    currentTextType = NEUTRAL;
                    reprocessCurrentCharacter = true;
                }

                // Hex number start
                else if (strcmp(codeLine, "0x", i) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-number'>");
                    currentTextType = HEX_NUMBER_PREFIX;
                }

                // Hex number start x
                else if (currentTextType == HEX_NUMBER_PREFIX && c == 'x') {
                    currentTextType = HEX_NUMBER;
                }

                // Hex number end
                else if (currentTextType == HEX_NUMBER && (!Character.isDigit(c)
                        && !(c >= 'a' && c <= 'f')
                        && !(c >= 'A' && c <= 'F')
                )) {
                    sb.append("</span>");
                    currentTextType = NEUTRAL;
                    reprocessCurrentCharacter = true;
                }


                // Number start
                else if (Character.isDigit(c) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-number'>");
                    currentTextType = NUMBER;
                }

                // Decimal part of number
                else if (c == '.' && currentTextType == NUMBER) {
                    currentTextType = DECIMAL_PART_NUMBER;
                }

                // Number end
                else if (!Character.isDigit(c) && (currentTextType == NUMBER || currentTextType == DECIMAL_PART_NUMBER)) {
                    sb.append("</span>");
                    currentTextType = NEUTRAL;
                    reprocessCurrentCharacter = true;
                }

                // Operator
                else if (!Character.isWhitespace(c) && isOperator(codeLine, i) && currentTextType == NEUTRAL) {
                    sb.append("<span class='code-highlight-operator'>");
                    spanEndsAfterwards = true;
                    currentTextType = NEUTRAL;
                }

                if (reprocessCurrentCharacter) continue;

                sb.append(c);
                i++;

                if (spanEndsAfterwards) {
                    sb.append("</span>");
                }
            }

            // If cursor is on last position within line, append here:
            if (cursorLine == lineNumber && cursorCol == codeLine.length()) {
                sb.append("<span class='cursor-container'><span id='cursor'></span></span>");
            }

            sb.append("</tr>\n");
        }

        sb.append("\n</table>\n" +
                "<script>\n" +
                "    let cursor = document.getElementById('cursor');\n" +
                "    let old;\n" +
                "    setInterval(() => {\n" +
                "        let now = Math.trunc(new Date().getMilliseconds() / 500);\n" +
                "        if(now !== old) {\n" +
                "            cursor.style.visibility = cursor.style.visibility === 'visible' ? 'hidden' : 'visible';\n" +
                "            old = now;\n" +
                "        }\n" +
                "    }, 100);" +
                "</script>\n" +
                "</body></html>");

        return sb.toString();
    }

    private boolean isKeyword(String code, int position) {
        code = code.substring(position).replaceFirst("^([a-z]+).*$", "$1");
        for (String item : KEYWORD_LIST) {
            if (code.equals(item)) return true;
        }
        return false;
    }

    private boolean isOperator(String code, int position) {
        for (char c : OPERATOR_LIST) {
            if (code.charAt(position) == c)
                return true;
        }
        return false;
    }

}
