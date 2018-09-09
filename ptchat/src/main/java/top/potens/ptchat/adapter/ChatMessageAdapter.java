package top.potens.ptchat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.potens.ptchat.GlobalStaticVariable;
import top.potens.ptchat.R;
import top.potens.ptchat.bean.MessageBean;
import top.potens.ptchat.engine.PtchatImageEngine;
import top.potens.ptchat.helper.FaceHelper;
import top.potens.ptchat.view.VoicePlayingView;


/**
 * Created by wenshao on 2017/4/10.
 * 聊天室聊天记录适配器
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BaseAdapter> {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageAdapter.class);

    private List<MessageBean> dataList = new ArrayList<>();
    private Drawable soundDrawable;
    private OnMessageItemClickListener mOnMessageItemClickListener;
    private Context mContext;
    private final static String fileRegex = "^file://.*";
    private PtchatImageEngine mPtchatImageEngine = GlobalStaticVariable.getPtchat().getChatImageEngine();
    private Map<String, TextView> textViewMapSendCode = new HashMap<>();


    public ChatMessageAdapter(Context context) {

        this.mContext = context;
        soundDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_action_sound);
        soundDrawable.setBounds(0, 0, soundDrawable.getMinimumWidth(), soundDrawable.getMinimumHeight());


    }

    public void replaceAll(List<MessageBean> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<MessageBean> list) {
        if (dataList != null && list != null) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
        notifyDataSetChanged();
    }

    public void add(MessageBean messageBean) {
        if (messageBean != null) {
            dataList.add(messageBean);
            notifyItemRangeChanged(dataList.size(), 1);
        }
        notifyDataSetChanged();
    }

    public List<MessageBean> getDataList() {
        return dataList;
    }

    @Override
    public BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MessageBean.LOCATION_LEFT:
                return new ChatLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_left, parent, false), mOnMessageItemClickListener);
            case MessageBean.LOCATION_RIGHT:
                return new ChatRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_right, parent, false), mOnMessageItemClickListener);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(BaseAdapter holder, int position) {
        holder.setData(dataList.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getLocation();
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(MessageBean message) {

        }


    }

    /**
     * 设置消息的状态
     * @param sendCode      sendCode
     * @param status        状态
     */
    public void setMessageStatus(String sendCode, int status) {
        if (textViewMapSendCode.containsKey(sendCode)) {
            textViewMapSendCode.get(sendCode).setText(mContext.getString(MessageBean.getStatusResourceId(status)));
        }

    }
    private class ChatLeftViewHolder extends BaseAdapter implements View.OnTouchListener {
        private ImageView rw_head;
        private TextView tv_content;
        private ImageView rw_content;
        private VoicePlayingView vpv_audio;
        private GestureDetector mGestureDetector;



        public ChatLeftViewHolder(View view, OnMessageItemClickListener listener) {
            super(view);
            rw_head = view.findViewById(R.id.rw_head);
            tv_content = view.findViewById(R.id.tv_content);
            rw_content = view.findViewById(R.id.rw_content);
            vpv_audio = view.findViewById(R.id.vpv_audio);


            mGestureDetector = new GestureDetector(mContext, new DefaultGestureListener(listener));
            view.setOnTouchListener(this);
        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String head = messageBean.getFriendUserBean().getUserHead();

            mPtchatImageEngine.loadChatHead(mContext, head, rw_head);
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)) {
                SpannableStringBuilder sb = FaceHelper.imageToGif(mContext, tv_content, messageBean.getContent());

                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);
                tv_content.setVisibility(View.VISIBLE);
                tv_content.setText(sb);
            } else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)) {
                tv_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.GONE);

                rw_content.setVisibility(View.VISIBLE);
                mPtchatImageEngine.loadChatImage(mContext, messageBean.getContent(), rw_content);
            } else if (messageBean.getType().equals(MessageBean.TYPE_AUDIO)) {
                tv_content.setVisibility(View.GONE);
                rw_content.setVisibility(View.GONE);
                vpv_audio.setVisibility(View.VISIBLE);
                vpv_audio.setDuration(messageBean.getDuration());


            }

        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            motionEvent.setEdgeFlags(getAdapterPosition());
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

    }

    private class ChatRightViewHolder extends BaseAdapter implements View.OnTouchListener {
        private RelativeLayout relative_tv_content;
        private RelativeLayout relative_vpv_audio;
        private RelativeLayout relative_rw_content;
        private ImageView rw_head;
        private TextView tv_content;
        private ImageView rw_content;
        private VoicePlayingView vpv_audio;
        private GestureDetector mGestureDetector;
        private TextView tv_content_status;
        private TextView rw_content_status;
        private TextView vpv_audio_status;

        public ChatRightViewHolder(View view, OnMessageItemClickListener listener) {
            super(view);
            RelativeLayout ry_message_main = view.findViewById(R.id.ry_message_main);


            relative_tv_content = view.findViewById(R.id.relative_tv_content);
            relative_vpv_audio = view.findViewById(R.id.relative_vpv_audio);
            relative_rw_content = view.findViewById(R.id.relative_rw_content);

            rw_head = view.findViewById(R.id.rw_head);

            tv_content = view.findViewById(R.id.tv_content);
            rw_content = view.findViewById(R.id.rw_content);
            vpv_audio = view.findViewById(R.id.vpv_audio);

            tv_content_status = view.findViewById(R.id.tv_content_status);
            rw_content_status = view.findViewById(R.id.rw_content_status);
            vpv_audio_status = view.findViewById(R.id.vpv_audio_status);
            mGestureDetector = new GestureDetector(mContext, new DefaultGestureListener(listener));
            view.setOnTouchListener(this);

        }

        @Override
        void setData(MessageBean messageBean) {
            super.setData(messageBean);
            String head = messageBean.getFriendUserBean().getUserHead();
            mPtchatImageEngine.loadChatHead(mContext, head, rw_head);
            // 判断消息的类型
            if (messageBean.getType().equals(MessageBean.TYPE_TEXT)) {
                SpannableStringBuilder sb = FaceHelper.imageToGif(mContext, tv_content, messageBean.getContent());
                relative_rw_content.setVisibility(View.GONE);
                relative_vpv_audio.setVisibility(View.GONE);
                relative_tv_content.setVisibility(View.VISIBLE);
                tv_content.setText(sb);
                textViewMapSendCode.put(messageBean.getSendCode(),tv_content_status);
            } else if (messageBean.getType().equals(MessageBean.TYPE_IMAGE)) {
                relative_tv_content.setVisibility(View.GONE);
                relative_vpv_audio.setVisibility(View.GONE);
                relative_rw_content.setVisibility(View.VISIBLE);
                mPtchatImageEngine.loadChatImage(mContext, messageBean.getContent(), rw_content);
                textViewMapSendCode.put(messageBean.getSendCode(), rw_content_status);
            } else if (messageBean.getType().equals(MessageBean.TYPE_AUDIO)) {
                relative_tv_content.setVisibility(View.GONE);
                relative_rw_content.setVisibility(View.GONE);
                relative_vpv_audio.setVisibility(View.VISIBLE);
                vpv_audio.setDuration(messageBean.getDuration());
                textViewMapSendCode.put(messageBean.getSendCode(), vpv_audio_status);
            }
            setMessageStatus(messageBean.getSendCode(), messageBean.getStatus());


        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            motionEvent.setEdgeFlags(getAdapterPosition());
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    /**
     * 设置chat item的点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnMessageItemClickListener listener) {
        this.mOnMessageItemClickListener = listener;
    }

    // item点击事件监听接口
    public interface OnMessageItemClickListener {
        public void onMessageItemClick(int position);
        public void onMessageItemDoubleClick(int position);
    }

    private class DefaultGestureListener extends GestureDetector.SimpleOnGestureListener {
        private OnMessageItemClickListener eventOnMessageItemClickListener;
        public DefaultGestureListener(OnMessageItemClickListener onMessageItemClickListener){
            this.eventOnMessageItemClickListener = onMessageItemClickListener;
        }
        // ================================== 触发顺序： onDown->onShowPress->onLongPress


        /**
         * 用户触发DownEvent就会执行
         *
         * @param e event
         * @return boolean
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        /**
         * 用户触发DownEvent后，在很短大概0.5秒内，没有触发其他MoveEvent和UpEvent事件，会执行
         *
         * @param e event
         */
        @Override
        public void onShowPress(MotionEvent e) {
            logger.debug("onSingleTapUp");

        }

        /**
         * 用户触发DownEvent后，在很短大概1.5秒内，没有触发其他MoveEvent和UpEvent事件，会执行
         *
         * @param e event
         */
        @Override
        public void onLongPress(MotionEvent e) {
            logger.debug("onSingleTapUp");
        }

        //==============================================


        //==============================================
        // 点击一下非常快的（不滑动）TouchUp：
        // onDown->onSingleTapUp->onSingleTapConfirmed
        // 点击一下稍微慢点的（不滑动）TouchUp：
        // onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (this.eventOnMessageItemClickListener != null)
                this.eventOnMessageItemClickListener.onMessageItemClick(e.getEdgeFlags());
            return false;
        }


        /**
         * @param e1        The first down motion event that started the scrolling.
         * @param e2        The move motion event that triggered the current onScroll.
         * @param distanceX The distance along the X axis(轴) that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
         * @param distanceY The distance along the Y axis that has been scrolled since the last call to onScroll. This is NOT the distance between e1 and e2.
         *                  无论是用手拖动view，或者是以抛的动作滚动，都会多次触发 ,这个方法在ACTION_MOVE动作发生时就会触发 参看GestureDetector的onTouchEvent方法源码
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return false;
        }

        /**
         * @param e1        第1个ACTION_DOWN MotionEvent 并且只有一个
         * @param e2        最后一个ACTION_MOVE MotionEvent
         * @param velocityX X轴上的移动速度，像素/秒
         * @param velocityY Y轴上的移动速度，像素/秒
         *                  这个方法发生在ACTION_UP时才会触发 参看GestureDetector的onTouchEvent方法源码
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        // 在双击的第二下，Touch down时触发 。
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            logger.debug("=======onDoubleTap");

            return false;
        }

        // 通知DoubleTap手势中的事件，包含down、up和move事件（这里指的是在双击之间发生的事件，
        // 例如在同一个地方双击会产生DoubleTap手势，而在DoubleTap手势里面还会发生down和up事件，
        // 这两个事件由该函数通知）；双击的第二下Touch down和up都会触发，可用e.getAction()区分。
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (this.eventOnMessageItemClickListener != null)
                this.eventOnMessageItemClickListener.onMessageItemDoubleClick(e.getEdgeFlags());
            return false;
        }

        /**
         * 这个方法不同于onSingleTapUp，他是在GestureDetector确信用户在第一次触摸屏幕后，没有紧跟着第二次触摸屏幕，也就是不是“双击”的时候触发
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

    }
}
