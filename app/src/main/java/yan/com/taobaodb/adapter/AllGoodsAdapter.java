package yan.com.taobaodb.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.ArrayList;

import yan.com.taobaodb.R;
import yan.com.taobaodb.databsse.TBDataBase;
import yan.com.taobaodb.model.Goods;

/**
 * 显示所有商品
 * Created by yan on 2015/11/1.
 */
public class AllGoodsAdapter extends RecyclerSwipeAdapter<AllGoodsAdapter.SimpleViewHolder> {
    private Context mContext;
    private ArrayList<Goods> myDatas;
    private TBDataBase tbDataBase = TBDataBase.getInstance(mContext);
    private int praise;


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_watch_goods_list_item,viewGroup,false);
        return new AllGoodsAdapter.SimpleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder simpleViewHolder, final int i) {

        //读取数据
        final String goodsId = myDatas.get(i).getGId();
        String goodsMid = myDatas.get(i).getMId();
        final String goodsName = myDatas.get(i).getGName();
        String goodsPrice = myDatas.get(i).getGPrice();
        String goodsIntroduction = myDatas.get(i).getGIntroduction();
        final String goodsPraise = String.valueOf(myDatas.get(i).getGPraise());
        byte[] picture = myDatas.get(i).getGPicture();
        praise = Integer.parseInt(goodsPraise);
        String merchantsName = tbDataBase.loadAGoodsMerchants(goodsMid);

        //设置模式等
        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {

            }
        });

        /***
         * 左滑三个图标的实现和监听
         * */
        //左滑三个图标的实现和监听
        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, simpleViewHolder.swipeLayout.findViewWithTag("Bottom2"));

        //第二个图标监听
        simpleViewHolder.swipeLayout.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleViewHolder.goodsPraise.setImageResource(R.drawable.starall);
                praise = praise +1;
                tbDataBase.setGpraise(goodsId,praise);
                Toast.makeText(mContext, "Star", Toast.LENGTH_SHORT).show();
                simpleViewHolder.swipeLayout.findViewById(R.id.star).setEnabled(false);
            }
        });

        //第三个图标的监听
        simpleViewHolder.swipeLayout.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(mContext, "Trash Bin", Toast.LENGTH_SHORT).show();mItemManger.removeShownLayouts(simpleViewHolder.swipeLayout);
                // mDataset.remove(i);
                // notifyItemRemoved(i);
                //  mItemManger.closeAllItems();
                //  Toast.makeText(v.getContext(), "Deleted " + simpleViewHolder.textViewData.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                //创建一个弹窗 警告是否要删除数据
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("确认删除商品吗");
                builder.setMessage("你确认要删除名为 " + goodsName + "的商品吗？该操作无法恢复！");
                builder.setCancelable(false);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDatas.remove(i);
                        notifyItemRemoved(i);
                        mItemManger.closeAllItems();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        //第一个图标的监听
        simpleViewHolder.swipeLayout.findViewById(R.id.magnifier).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Magnifier", Toast.LENGTH_SHORT).show();
            }
        });

        //这里实现了列表的监听
        simpleViewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click on surface" + i, Toast.LENGTH_SHORT).show();
            }
        });



        //加载数据

        simpleViewHolder.goodsNameTextView.setText(goodsName);
        simpleViewHolder.goodsPriceTextView.setText(goodsPrice);
        simpleViewHolder.goodsIntroduction.setText(goodsIntroduction);

        simpleViewHolder.goodsMerchants.setText(merchantsName);

        //把二进制代码转成位图
        Bitmap pictures = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        simpleViewHolder.goodsPicture.setImageBitmap(pictures);

        mItemManger.bindView(simpleViewHolder.itemView, i);

    }


    @Override
    public int getItemCount() {
        return myDatas.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }


    //加载数据的类
    public static class SimpleViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout swipeLayout;

        TextView goodsNameTextView;
        ImageView goodsPraise;
        TextView goodsPriceTextView;
        ImageView goodsPicture;
        TextView goodsIntroduction;
        TextView goodsMerchants;

        public SimpleViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            goodsNameTextView = (TextView) itemView.findViewById(R.id.goods_name);
            goodsPriceTextView = (TextView) itemView.findViewById(R.id.goods_price);
            goodsIntroduction = (TextView) itemView.findViewById(R.id.goods_introduction);
            goodsPicture = (ImageView) itemView.findViewById(R.id.goods_picture_bitmap);
            goodsMerchants = (TextView) itemView.findViewById(R.id.magnifier);
            goodsPraise = (ImageView) itemView.findViewById(R.id.star);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });

        }
    }
    public AllGoodsAdapter(Context context,ArrayList<Goods> goods){
        this.mContext = context;
        this.myDatas = goods;
    }
}

