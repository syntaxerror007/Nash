package com.nash.android.util

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.LeadingMarginSpan
import org.xml.sax.XMLReader
import java.util.*

/**
 * Created by stephen on 7/28/15.
 */
class HtmlTagHandler : Html.TagHandler {
    /**
     * Keeps track of lists (ol, ul). On bottom of Stack is the outermost list
     * and on top of Stack is the most nested list
     */
    private var lists = Stack<String>()
    /**
     * Tracks indexes of ordered lists so that after a nested list ends
     * we can continue with correct index of outer list
     */
    private var olNextIndex = Stack<Int>()

    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag.equals("ul", ignoreCase = true)) {
            if (opening) {
                lists.push(tag)
            } else {
                lists.pop()
            }
        } else if (tag.equals("ol", ignoreCase = true)) {
            if (opening) {
                lists.push(tag)
                olNextIndex.push(Integer.valueOf(1)).toString()
            } else {
                lists.pop()
                olNextIndex.pop().toString()
            }
        } else if (tag.equals("li", ignoreCase = true)) {
            if (opening) {
                if (output.isNotEmpty() && output[output.length - 1] != '\n') {
                    output.append("\n")
                }
                val parentList = lists.peek()
                if (parentList.equals("ol", ignoreCase = true)) {
                    start(output, Ol())
                    output.append(olNextIndex.peek().toString() + ". ")
                    olNextIndex.push(Integer.valueOf(olNextIndex.pop().toInt() + 1))
                } else if (parentList.equals("ul", ignoreCase = true)) {
                    start(output, Ul())
                }
            } else {
                if (lists.peek().equals("ul", ignoreCase = true)) {
                    if (output.isNotEmpty() && output[output.length - 1] != '\n') {
                        output.append("\n")
                    }
                    // Nested BulletSpans increases distance between bullet and text, so we must prevent it.
                    var bulletMargin = indent
                    if (lists.size > 1) {
                        bulletMargin = indent - bullet.getLeadingMargin(true)
                        if (lists.size > 2) {
                            // This get's more complicated when we add a LeadingMarginSpan into the same line:
                            // we have also counter it's effect to BulletSpan
                            bulletMargin -= (lists.size - 2) * listItemIndent
                        }
                    }
                    val newBullet = BulletSpan(bulletMargin)
                    end(output,
                            Ul::class.java,
                            LeadingMarginSpan.Standard(listItemIndent * (lists.size - 1)),
                            newBullet)
                } else if (lists.peek().equals("ol", ignoreCase = true)) {
                    if (output.isNotEmpty() && output[output.length - 1] != '\n') {
                        output.append("\n")
                    }
                    var numberMargin = listItemIndent * (lists.size - 1)
                    if (lists.size > 2) {
                        // Same as in ordered lists: counter the effect of nested Spans
                        numberMargin -= (lists.size - 2) * listItemIndent
                    }
                    end(output,
                            Ol::class.java,
                            LeadingMarginSpan.Standard(numberMargin))
                }
            }
        } else {
            //if (opening) Log.d("TagHandler", "Found an unsupported tag " + tag);
        }
    }

    private class Ul

    private class Ol

    companion object {
        /**
         * List indentation in pixels. Nested lists use multiple of this.
         */
        private const val indent = 10
        private const val listItemIndent = indent * 2
        private val bullet = BulletSpan(indent)

        /**
         * @see android.text.Html
         */
        private fun start(text: Editable, mark: Any) {
            val len = text.length
            text.setSpan(mark, len, len, Spanned.SPAN_MARK_MARK)
        }

        /**
         * Modified from [android.text.Html]
         */
        private fun end(text: Editable, kind: Class<*>, vararg replaces: Any) {
            val len = text.length
            val obj = getLast(text, kind)
            val where = text.getSpanStart(obj)
            text.removeSpan(obj)
            if (where != len) {
                for (replace in replaces) {
                    text.setSpan(replace, where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            return
        }

        /**
         * @see android.text.Html
         */
        private fun getLast(text: Spanned, kind: Class<*>): Any? {
            /*
		 * This knows that the last returned object from getSpans()
		 * will be the most recently added.
		 */
            val objs = text.getSpans(0, text.length, kind)
            return if (objs.isEmpty()) {
                null
            } else objs[objs.size - 1]
        }
    }
}