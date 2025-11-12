package lab6;

import java.util.concurrent.Semaphore;

public class SynchronizedList {

    private Element head;

    public boolean contains(Object value) throws InterruptedException {
        if (head == null) {
            return false;
        }
        head.semaphore.acquire();
        Element cur = head;
        Element next = cur.next;

        while (next != null) {
            next.semaphore.acquire();
            Thread.sleep(10);
            if (cur.value.equals(value)) {
                cur.semaphore.release();
                next.semaphore.release();
                return true;
            }
            Element nextNext = next.next;
            cur.semaphore.release();
            cur = next;
            next = nextNext;
        }

        var lastEq = cur.value.equals(value);
        cur.semaphore.release();
        return lastEq;
    }

    public boolean add(Object value) throws InterruptedException {
        if (value == null) {
            return false;
        }

        if (head == null) {
            head = new Element(value);
            return true;
        }
        head.semaphore.acquire();
        Element cur = head;

        Element next = cur.next;

        while (next != null) {
            next.semaphore.acquire();
            Element nextNext = next.next;
            cur.semaphore.release();
            cur = next;
            next = nextNext;
        }

        cur.next = new Element(value);
        cur.semaphore.release();
        return true;
    }

    public boolean remove(Object value) throws InterruptedException {
        if (head == null) {
            return false;
        }
        head.semaphore.acquire();
        Element cur = head;

        if (cur.value.equals(value)) {
            if (cur.next != null) {
                cur.next.semaphore.acquire();
            }
            head = cur.next;
            cur.semaphore.release();
            if (cur.next != null) {
                cur.next.semaphore.release();
            }
            return true;
        }

        Element next = cur.next;

        while (next != null) {
            next.semaphore.acquire();
            if (next.value.equals(value)) {
                if (next.next != null) {
                    next.next.semaphore.acquire();
                }
                cur.next = next.next;
                cur.semaphore.release();
                next.semaphore.release();
                if (next.next != null) {
                    next.next.semaphore.release();
                }
                return true;
            }
            Element nextNext = next.next;
            cur.semaphore.release();
            cur = next;
            next = nextNext;
        }

        cur.semaphore.release();
        return false;
    }


    private class Element {

        public Element next;
        public Object value;
        public Semaphore semaphore = new Semaphore(1);

        public Element(Object value) {
            this.value = value;
        }

    }

}
