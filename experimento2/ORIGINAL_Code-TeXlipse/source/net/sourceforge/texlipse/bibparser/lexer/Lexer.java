/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.lexer;

import java.io.*;
import net.sourceforge.texlipse.bibparser.node.*;

@SuppressWarnings("nls")
public class Lexer
{
    protected Token token;
    protected State state = State.NORMAL;

    private PushbackReader in;
    private int line;
    private int pos;
    private boolean cr;
    private boolean eof;
    private final StringBuffer text = new StringBuffer();

    @SuppressWarnings("unused")
    protected void filter() throws LexerException, IOException
    {
        // Do nothing
    }

    public Lexer(@SuppressWarnings("hiding") PushbackReader in)
    {
        this.in = in;
    }
 
    public Token peek() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        return this.token;
    }

    public Token next() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.token = getToken();
            filter();
        }

        Token result = this.token;
        this.token = null;
        return result;
    }

    protected Token getToken() throws IOException, LexerException
    {
        int dfa_state = 0;

        int start_pos = this.pos;
        int start_line = this.line;

        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;

        @SuppressWarnings("hiding") int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
        @SuppressWarnings("hiding") int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);

        while(true)
        {
            int c = getChar();

            if(c != -1)
            {
                switch(c)
                {
                case 10:
                    if(this.cr)
                    {
                        this.cr = false;
                    }
                    else
                    {
                        this.line++;
                        this.pos = 0;
                    }
                    break;
                case 13:
                    this.line++;
                    this.pos = 0;
                    this.cr = true;
                    break;
                default:
                    this.pos++;
                    this.cr = false;
                    break;
                }

                this.text.append((char) c);

                do
                {
                    int oldState = (dfa_state < -1) ? (-2 -dfa_state) : dfa_state;

                    dfa_state = -1;

                    int[][] tmp1 =  gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;

                    while(low <= high)
                    {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];

                        if(c < tmp2[0])
                        {
                            high = middle - 1;
                        }
                        else if(c > tmp2[1])
                        {
                            low = middle + 1;
                        }
                        else
                        {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                }while(dfa_state < -1);
            }
            else
            {
                dfa_state = -1;
            }

            if(dfa_state >= 0)
            {
                if(accept[dfa_state] != -1)
                {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            }
            else
            {
                if(accept_state != -1)
                {
                    switch(accept_token)
                    {
                    case 0:
                        {
                            @SuppressWarnings("hiding") Token token = new0(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.NORMAL; break;
                            }
                            return token;
                        }
                    case 1:
                        {
                            @SuppressWarnings("hiding") Token token = new1(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.ASSIGN; break;
                                case 3: state = State.BRACESTRING; break;
                                case 1: state = State.INENTRY; break;
                                case 5: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 2:
                        {
                            @SuppressWarnings("hiding") Token token = new2(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.INENTRY; break;
                            }
                            return token;
                        }
                    case 3:
                        {
                            @SuppressWarnings("hiding") Token token = new3(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 4:
                        {
                            @SuppressWarnings("hiding") Token token = new4(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 5:
                        {
                            @SuppressWarnings("hiding") Token token = new5(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.INENTRY; break;
                            }
                            return token;
                        }
                    case 6:
                        {
                            @SuppressWarnings("hiding") Token token = new6(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 0: state = State.NORMAL; break;
                            }
                            return token;
                        }
                    case 7:
                        {
                            @SuppressWarnings("hiding") Token token = new7(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.BRACESTRING; break;
                                case 3: state = State.BRACESTRING; break;
                                case 1: state = State.INENTRY; break;
                                case 5: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 8:
                        {
                            @SuppressWarnings("hiding") Token token = new8(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.NORMAL; break;
                                case 3: state = State.BRACESTRING; break;
                                case 1: state = State.NORMAL; break;
                                case 5: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 9:
                        {
                            @SuppressWarnings("hiding") Token token = new9(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 3: state = State.BRACESTRING; break;
                                case 5: state = State.REMOVE; break;
                            }
                            return token;
                        }
                    case 10:
                        {
                            @SuppressWarnings("hiding") Token token = new10(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 1: state = State.INENTRY; break;
                            }
                            return token;
                        }
                    case 11:
                        {
                            @SuppressWarnings("hiding") Token token = new11(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.NORMAL; break;
                                case 1: state = State.NORMAL; break;
                            }
                            return token;
                        }
                    case 12:
                        {
                            @SuppressWarnings("hiding") Token token = new12(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.INENTRY; break;
                                case 1: state = State.INENTRY; break;
                            }
                            return token;
                        }
                    case 13:
                        {
                            @SuppressWarnings("hiding") Token token = new13(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 1: state = State.ASSIGN; break;
                            }
                            return token;
                        }
                    case 14:
                        {
                            @SuppressWarnings("hiding") Token token = new14(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.ASSIGN; break;
                            }
                            return token;
                        }
                    case 15:
                        {
                            @SuppressWarnings("hiding") Token token = new15(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.ASSIGN; break;
                            }
                            return token;
                        }
                    case 16:
                        {
                            @SuppressWarnings("hiding") Token token = new16(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.ASSIGN; break;
                                case 1: state = State.INENTRY; break;
                            }
                            return token;
                        }
                    case 17:
                        {
                            @SuppressWarnings("hiding") Token token = new17(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 2: state = State.QSTRING; break;
                                case 4: state = State.ASSIGN; break;
                            }
                            return token;
                        }
                    case 18:
                        {
                            @SuppressWarnings("hiding") Token token = new18(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            switch(state.id())
                            {
                                case 4: state = State.QSTRING; break;
                            }
                            return token;
                        }
                    }
                }
                else
                {
                    if(this.text.length() > 0)
                    {
                        throw new LexerException(
                            "[" + (start_line + 1) + "," + (start_pos + 1) + "]" +
                            " Unknown token: " + this.text);
                    }

                    @SuppressWarnings("hiding") EOF token = new EOF(
                        start_line + 1,
                        start_pos + 1);
                    return token;
                }
            }
        }
    }

    Token new0(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TTaskcomment(text, line, pos); }
    Token new1(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TWhitespace(text, line, pos); }
    Token new2(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TEstring(text, line, pos); }
    Token new3(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TScribeComment(text, line, pos); }
    Token new4(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TPreamble(text, line, pos); }
    Token new5(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TEntryName(text, line, pos); }
    Token new6(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TComment(text, line, pos); }
    Token new7(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TLBrace(line, pos); }
    Token new8(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TRBrace(line, pos); }
    Token new9(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TBString(text, line, pos); }
    Token new10(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TLParen(line, pos); }
    Token new11(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TRParen(line, pos); }
    Token new12(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TComma(line, pos); }
    Token new13(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TEquals(line, pos); }
    Token new14(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TSharp(line, pos); }
    Token new15(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TNumber(text, line, pos); }
    Token new16(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TIdentifier(text, line, pos); }
    Token new17(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TQuotec(line, pos); }
    Token new18(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) { return new TStringLiteral(text, line, pos); }

    private int getChar() throws IOException
    {
        if(this.eof)
        {
            return -1;
        }

        int result = this.in.read();

        if(result == -1)
        {
            this.eof = true;
        }

        return result;
    }

    private void pushBack(int acceptLength) throws IOException
    {
        int length = this.text.length();
        for(int i = length - 1; i >= acceptLength; i--)
        {
            this.eof = false;

            this.in.unread(this.text.charAt(i));
        }
    }

    protected void unread(@SuppressWarnings("hiding") Token token) throws IOException
    {
        @SuppressWarnings("hiding") String text = token.getText();
        int length = text.length();

        for(int i = length - 1; i >= 0; i--)
        {
            this.eof = false;

            this.in.unread(text.charAt(i));
        }

        this.pos = token.getPos() - 1;
        this.line = token.getLine() - 1;
    }

    private String getText(int acceptLength)
    {
        StringBuffer s = new StringBuffer(acceptLength);
        for(int i = 0; i < acceptLength; i++)
        {
            s.append(this.text.charAt(i));
        }

        return s.toString();
    }

    private static int[][][][] gotoTable;
/*  {
        { // NORMAL
            {{0, 36, 1}, {37, 37, 2}, {38, 63, 1}, {64, 64, 3}, {65, 65535, 1}, },
            {{0, 63, 1}, {65, 65535, 1}, },
            {{0, 8, 1}, {9, 9, 4}, {10, 31, 1}, {32, 32, 4}, {33, 63, 1}, {65, 83, 1}, {84, 84, 5}, {85, 65535, 1}, },
            {{65, 66, 6}, {67, 67, 7}, {68, 79, 6}, {80, 80, 8}, {81, 82, 6}, {83, 83, 9}, {84, 90, 6}, {97, 98, 10}, {99, 99, 11}, {100, 111, 10}, {112, 112, 12}, {113, 114, 10}, {115, 115, 13}, {116, 122, 10}, },
            {{0, 65535, -4}, },
            {{0, 63, 1}, {65, 78, 1}, {79, 79, 14}, {80, 65535, 1}, },
            {{65, 90, 6}, {97, 122, 10}, },
            {{65, 78, 6}, {79, 79, 15}, {80, 90, 6}, {97, 110, 10}, {111, 111, 16}, {112, 122, 10}, },
            {{65, 81, 6}, {82, 82, 17}, {83, 90, 6}, {97, 113, 10}, {114, 114, 18}, {115, 122, 10}, },
            {{65, 83, 6}, {84, 84, 19}, {85, 90, 6}, {97, 115, 10}, {116, 116, 20}, {117, 122, 10}, },
            {{65, 122, -8}, },
            {{65, 122, -9}, },
            {{65, 122, -10}, },
            {{65, 122, -11}, },
            {{0, 63, 1}, {65, 67, 1}, {68, 68, 21}, {69, 65535, 1}, },
            {{65, 76, 6}, {77, 77, 22}, {78, 90, 6}, {97, 108, 10}, {109, 109, 23}, {110, 122, 10}, },
            {{65, 122, -17}, },
            {{65, 68, 6}, {69, 69, 24}, {70, 90, 6}, {97, 100, 10}, {101, 101, 25}, {102, 122, 10}, },
            {{65, 122, -19}, },
            {{65, 81, 6}, {82, 82, 26}, {83, 113, -10}, {114, 114, 27}, {115, 122, 10}, },
            {{65, 122, -21}, },
            {{0, 78, -7}, {79, 79, 28}, {80, 65535, 1}, },
            {{65, 76, 6}, {77, 77, 29}, {78, 108, -17}, {109, 109, 30}, {110, 122, 10}, },
            {{65, 122, -24}, },
            {{65, 65, 31}, {66, 90, 6}, {97, 97, 32}, {98, 122, 10}, },
            {{65, 122, -26}, },
            {{65, 72, 6}, {73, 73, 33}, {74, 90, 6}, {97, 104, 10}, {105, 105, 34}, {106, 122, 10}, },
            {{65, 122, -28}, },
            {{0, 9, 35}, {10, 10, 36}, {11, 12, 35}, {13, 13, 37}, {14, 63, 35}, {64, 64, 38}, {65, 65535, 35}, },
            {{65, 68, 6}, {69, 69, 39}, {70, 100, -19}, {101, 101, 40}, {102, 122, 10}, },
            {{65, 122, -31}, },
            {{65, 76, 6}, {77, 77, 41}, {78, 108, -17}, {109, 109, 42}, {110, 122, 10}, },
            {{65, 122, -33}, },
            {{65, 77, 6}, {78, 78, 43}, {79, 90, 6}, {97, 109, 10}, {110, 110, 44}, {111, 122, 10}, },
            {{65, 122, -35}, },
            {{0, 65535, -30}, },
            {{0, 65535, -3}, },
            {{0, 9, 1}, {10, 10, 45}, {11, 63, 1}, {65, 65535, 1}, },
            {{0, 9, 38}, {10, 10, 46}, {11, 12, 38}, {13, 13, 47}, {14, 65535, 38}, },
            {{65, 77, 6}, {78, 78, 48}, {79, 109, -35}, {110, 110, 49}, {111, 122, 10}, },
            {{65, 122, -41}, },
            {{65, 65, 6}, {66, 66, 50}, {67, 90, 6}, {97, 97, 10}, {98, 98, 51}, {99, 122, 10}, },
            {{65, 122, -43}, },
            {{65, 70, 6}, {71, 71, 52}, {72, 90, 6}, {97, 102, 10}, {103, 103, 53}, {104, 122, 10}, },
            {{65, 122, -45}, },
            {{0, 65535, -3}, },
            {},
            {{10, 10, 54}, },
            {{65, 83, 6}, {84, 84, 55}, {85, 115, -11}, {116, 116, 56}, {117, 122, 10}, },
            {{65, 122, -50}, },
            {{65, 75, 6}, {76, 76, 57}, {77, 90, 6}, {97, 107, 10}, {108, 108, 58}, {109, 122, 10}, },
            {{65, 122, -52}, },
            {{65, 122, -8}, },
            {{65, 122, -8}, },
            {},
            {{65, 122, -8}, },
            {{65, 122, -8}, },
            {{65, 68, 6}, {69, 69, 59}, {70, 100, -19}, {101, 101, 60}, {102, 122, 10}, },
            {{65, 122, -59}, },
            {{65, 122, -8}, },
            {{65, 122, -8}, },
        }
        { // INENTRY
            {{0, 8, 1}, {9, 9, 2}, {10, 10, 3}, {11, 11, 1}, {12, 12, 4}, {13, 13, 5}, {14, 31, 1}, {32, 32, 6}, {33, 33, 1}, {36, 39, 1}, {40, 40, 7}, {41, 41, 8}, {42, 43, 1}, {44, 44, 9}, {45, 60, 1}, {61, 61, 10}, {62, 122, 1}, {123, 123, 11}, {124, 124, 1}, {125, 125, 12}, {126, 65535, 1}, },
            {{0, 8, 1}, {11, 11, 1}, {14, 31, 1}, {33, 39, -2}, {42, 43, 1}, {45, 60, 1}, {62, 122, 1}, {124, 124, 1}, {126, 65535, 1}, },
            {{9, 10, -2}, {12, 13, -2}, {32, 32, 6}, },
            {{9, 32, -4}, },
            {{9, 32, -4}, },
            {{9, 9, 2}, {10, 10, 13}, {12, 32, -4}, },
            {{9, 32, -4}, },
            {},
            {},
            {},
            {},
            {},
            {},
            {{9, 32, -4}, },
        }
        { // ASSIGN
            {{0, 8, 1}, {9, 9, 2}, {10, 10, 3}, {11, 11, 1}, {12, 12, 4}, {13, 13, 5}, {14, 31, 1}, {32, 32, 6}, {33, 33, 1}, {34, 34, 7}, {35, 35, 8}, {36, 39, 1}, {41, 41, 9}, {42, 43, 1}, {44, 44, 10}, {45, 47, 1}, {48, 57, 11}, {58, 60, 1}, {62, 122, 1}, {123, 123, 12}, {124, 124, 1}, {125, 125, 13}, {126, 65535, 1}, },
            {{0, 8, 1}, {11, 11, 1}, {14, 31, 1}, {33, 33, 1}, {36, 39, 1}, {42, 43, 1}, {45, 60, 1}, {62, 122, 1}, {124, 124, 1}, {126, 65535, 1}, },
            {{9, 10, -2}, {12, 13, -2}, {32, 32, 6}, },
            {{9, 32, -4}, },
            {{9, 32, -4}, },
            {{9, 9, 2}, {10, 10, 14}, {12, 32, -4}, },
            {{9, 32, -4}, },
            {},
            {},
            {},
            {},
            {{0, 43, -3}, {45, 122, -2}, {124, 65535, -3}, },
            {},
            {},
            {{9, 32, -4}, },
        }
        { // BRACESTRING
            {{0, 8, 1}, {9, 9, 2}, {10, 10, 3}, {11, 11, 1}, {12, 12, 4}, {13, 13, 5}, {14, 31, 1}, {32, 32, 6}, {33, 122, 1}, {123, 123, 7}, {124, 124, 1}, {125, 125, 8}, {126, 65535, 1}, },
            {{0, 8, 1}, {11, 11, 1}, {14, 31, 1}, {33, 122, 1}, {124, 124, 1}, {126, 65535, 1}, },
            {{9, 10, -2}, {12, 13, -2}, {32, 32, 6}, },
            {{9, 32, -4}, },
            {{9, 32, -4}, },
            {{9, 9, 2}, {10, 10, 9}, {12, 32, -4}, },
            {{9, 32, -4}, },
            {},
            {},
            {{9, 32, -4}, },
        }
        { // QSTRING
            {{0, 33, 1}, {34, 34, 2}, {35, 91, 1}, {92, 92, 3}, {93, 65535, 1}, },
            {{0, 33, 1}, {35, 65535, -2}, },
            {},
            {{0, 33, 1}, {34, 34, 4}, {35, 65535, -2}, },
            {{0, 65535, -3}, },
        }
        { // REMOVE
            {{0, 8, 1}, {9, 9, 2}, {10, 10, 3}, {11, 11, 1}, {12, 12, 4}, {13, 13, 5}, {14, 31, 1}, {32, 32, 6}, {33, 122, 1}, {123, 123, 7}, {124, 124, 1}, {125, 125, 8}, {126, 65535, 1}, },
            {{0, 8, 1}, {11, 11, 1}, {14, 31, 1}, {33, 122, 1}, {124, 124, 1}, {126, 65535, 1}, },
            {{9, 10, -2}, {12, 13, -2}, {32, 32, 6}, },
            {{9, 32, -4}, },
            {{9, 32, -4}, },
            {{9, 9, 2}, {10, 10, 9}, {12, 32, -4}, },
            {{9, 32, -4}, },
            {},
            {},
            {{9, 32, -4}, },
        }
    };*/

    private static int[][] accept;
/*  {
        // NORMAL
        {6, 6, 6, -1, 6, 6, 5, 5, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 6, 0, 0, -1, 5, 5, 5, 5, 5, 5, 0, 0, 0, 5, 5, 5, 5, 2, 2, 0, 3, 3, 5, 5, 4, 4, },
        // INENTRY
        {-1, 16, 1, 1, 1, 1, 1, 10, 11, 12, 13, 7, 8, 1, },
        // ASSIGN
        {-1, 16, 1, 1, 1, 1, 1, 17, 14, 11, 12, 15, 7, 8, 1, },
        // BRACESTRING
        {-1, 9, 1, 1, 1, 1, 1, 7, 8, 1, },
        // QSTRING
        {18, 18, 17, 18, 18, },
        // REMOVE
        {-1, 9, 1, 1, 1, 1, 1, 7, 8, 1, },

    };*/

    public static class State
    {
        public final static State NORMAL = new State(0);
        public final static State INENTRY = new State(1);
        public final static State ASSIGN = new State(2);
        public final static State BRACESTRING = new State(3);
        public final static State QSTRING = new State(4);
        public final static State REMOVE = new State(5);

        private int id;

        private State(@SuppressWarnings("hiding") int id)
        {
            this.id = id;
        }

        public int id()
        {
            return this.id;
        }
    }

    static 
    {
        try
        {
            DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                Lexer.class.getResourceAsStream("lexer.dat")));

            // read gotoTable
            int length = s.readInt();
            gotoTable = new int[length][][][];
            for(int i = 0; i < gotoTable.length; i++)
            {
                length = s.readInt();
                gotoTable[i] = new int[length][][];
                for(int j = 0; j < gotoTable[i].length; j++)
                {
                    length = s.readInt();
                    gotoTable[i][j] = new int[length][3];
                    for(int k = 0; k < gotoTable[i][j].length; k++)
                    {
                        for(int l = 0; l < 3; l++)
                        {
                            gotoTable[i][j][k][l] = s.readInt();
                        }
                    }
                }
            }

            // read accept
            length = s.readInt();
            accept = new int[length][];
            for(int i = 0; i < accept.length; i++)
            {
                length = s.readInt();
                accept[i] = new int[length];
                for(int j = 0; j < accept[i].length; j++)
                {
                    accept[i][j] = s.readInt();
                }
            }

            s.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
        }
    }
}
