package cf.tilgiz.parse.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cf.tilgiz.parse.pojo.Car;
import cf.tilgiz.parse.R;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> {
    private ArrayList<Car> carList;
    private OnCarClickListener onCarClickListener;

    public CarsAdapter(ArrayList<Car> carList) {
        this.carList = carList;
    }

    public interface OnCarClickListener{
        void onCarClick(int position);
    }

    public void setOnCarClickListener(OnCarClickListener onCarClickListener) {
        this.onCarClickListener = onCarClickListener;
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int car_item_layout_id = R.layout.car_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(car_item_layout_id, parent, false);
        return new CarsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsViewHolder carsViewHolder, int position) {
        Car car = carList.get(position);
        carsViewHolder.textViewName.setText(car.getName());
        carsViewHolder.textViewMileage.setText(String.valueOf(car.getMileage()));
        carsViewHolder.textViewYear.setText(String.valueOf(car.getYear()));
        carsViewHolder.textViewPrice.setText(String.valueOf(car.getPrice()));
        Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImageBytes(), 0, car.getImageBytes().length);
        carsViewHolder.imageViewPicture.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    class CarsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewMileage;
        private TextView textViewYear;
        private TextView textViewPrice;
        private ImageView imageViewPicture;

        CarsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewYear = itemView.findViewById(R.id.textViewYear);
            textViewMileage = itemView.findViewById(R.id.textViewMileage);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewPicture = itemView.findViewById(R.id.imageViewPicture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onCarClickListener != null)
                        onCarClickListener.onCarClick(getAdapterPosition());
                }
            });
        }
    }
}
